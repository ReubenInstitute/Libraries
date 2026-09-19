package Clock;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import java.util.HashMap;
import java.util.Map;

public class Display {
	private static final Map<String, Paint> paintCache = new HashMap<>();

	public final int WIDTH;
	public final int HEIGHT;
	public final byte[] buffer;

	public Display() {
		this(240);
	}

	public Display(int size) {
		this.WIDTH = size;
		this.HEIGHT = size;
		this.buffer = new byte[WIDTH * HEIGHT * 4];
	}

	public void clear() {
		java.util.Arrays.fill(buffer, (byte) 0);
	}

	public void fill(int color) {
		byte r = (byte) ((color >> 16) & 0xFF);
		byte g = (byte) ((color >> 8) & 0xFF);
		byte b = (byte) (color & 0xFF);
		for (int i = 0; i < buffer.length; i += 4) {
			buffer[i] = r;
			buffer[i + 1] = g;
			buffer[i + 2] = b;
			buffer[i + 3] = (byte) 255;
		}
	}

	// ARGB pixels, ready for Bitmap.setPixels
	public int[] toArgb() {
		int[] pixels = new int[WIDTH * HEIGHT];
		for (int i = 0; i < pixels.length; i++) {
			int idx = i * 4;
			pixels[i] = ((buffer[idx + 3] & 0xFF) << 24)
					| ((buffer[idx] & 0xFF) << 16)
					| ((buffer[idx + 1] & 0xFF) << 8)
					| (buffer[idx + 2] & 0xFF);
		}
		return pixels;
	}

	private void blend(int idx, int fr, int fg, int fb, double alpha) {
		if (alpha <= 0.0) {
			return;
		}
		if (alpha > 1.0) {
			alpha = 1.0;
		}

		double ba = (buffer[idx + 3] & 0xFF) / 255.0;
		double outA = alpha + ba * (1.0 - alpha);

		if (outA <= 0.0) {
			buffer[idx] = 0;
			buffer[idx + 1] = 0;
			buffer[idx + 2] = 0;
			buffer[idx + 3] = 0;
			return;
		}

		if (alpha >= 1.0) {
			buffer[idx] = (byte) fr;
			buffer[idx + 1] = (byte) fg;
			buffer[idx + 2] = (byte) fb;
			buffer[idx + 3] = (byte) 255;
			return;
		}

		int br = buffer[idx] & 0xFF;
		int bg = buffer[idx + 1] & 0xFF;
		int bb = buffer[idx + 2] & 0xFF;
		double remainder = ba * (1.0 - alpha);

		double outR = (fr * alpha + br * remainder) / outA;
		double outG = (fg * alpha + bg * remainder) / outA;
		double outB = (fb * alpha + bb * remainder) / outA;

		buffer[idx] = (byte) (int) (outR + 0.5);
		buffer[idx + 1] = (byte) (int) (outG + 0.5);
		buffer[idx + 2] = (byte) (int) (outB + 0.5);
		buffer[idx + 3] = (byte) (int) (outA * 255.0 + 0.5);
	}

	private static double mod(double x, double m) {
		double r = x % m;
		return r < 0 ? r + m : r;
	}

	// r is the signed distance from the shape edge, in pixels
	private static double coverage(double r) {
		if (r <= -1.0) return 1.0;
		if (r >= 1.0) return 0.0;
		return 1.0 - (r + 1.0) * 0.5;
	}

	private static double pointToSegmentDistance(double px, double py, double x1, double y1, double x2, double y2) {
		double dx = x2 - x1;
		double dy = y2 - y1;
		double lenSq = dx * dx + dy * dy;
		if (lenSq == 0.0) {
			double ex = px - x1;
			double ey = py - y1;
			return Math.sqrt(ex * ex + ey * ey);
		}
		double t = ((px - x1) * dx + (py - y1) * dy) / lenSq;
		if (t < 0.0) t = 0.0;
		if (t > 1.0) t = 1.0;
		double projx = x1 + t * dx;
		double projy = y1 + t * dy;
		double ex = px - projx;
		double ey = py - projy;
		return Math.sqrt(ex * ex + ey * ey);
	}

	private static double pointToArcDistance(double px, double py, double cx, double cy, double radius,
			double startAngle, double endAngle, double halfWidth) {
		double dx = px - cx;
		double dy = py - cy;
		double distToCenter = Math.sqrt(dx * dx + dy * dy);

		if (endAngle - startAngle >= 360.0) {
			return Math.abs(distToCenter - radius);
		}

		double sr = mod(Math.toRadians(startAngle), 2.0 * Math.PI);
		double er = mod(Math.toRadians(endAngle), 2.0 * Math.PI);

		boolean inRange;
		if (distToCenter == 0.0) {
			double a0 = 0.0;
			if (startAngle <= endAngle) {
				inRange = a0 >= startAngle && a0 <= endAngle;
			} else {
				inRange = a0 >= startAngle || a0 <= endAngle;
			}
		} else {
			double angle = Math.atan2(dy, dx);
			if (angle < 0.0) {
				angle += 2.0 * Math.PI;
			}
			if (sr <= er) {
				inRange = angle >= sr && angle <= er;
			} else {
				inRange = angle >= sr || angle <= er;
			}
		}

		if (inRange) {
			return Math.abs(distToCenter - radius);
		}

		double innerR = radius - halfWidth;
		double outerR = radius + halfWidth;

		double sxi = cx + innerR * Math.cos(sr);
		double syi = cy + innerR * Math.sin(sr);
		double sxo = cx + outerR * Math.cos(sr);
		double syo = cy + outerR * Math.sin(sr);

		double exi = cx + innerR * Math.cos(er);
		double eyi = cy + innerR * Math.sin(er);
		double exo = cx + outerR * Math.cos(er);
		double eyo = cy + outerR * Math.sin(er);

		double d1 = pointToSegmentDistance(px, py, sxi, syi, sxo, syo);
		double d2 = pointToSegmentDistance(px, py, exi, eyi, exo, eyo);
		double dmin = d1 < d2 ? d1 : d2;

		return dmin + halfWidth;
	}

	public void drawLine(int x1, int y1, int x2, int y2, double thickness, int color) {
		int fr = (color >> 16) & 0xFF;
		int fg = (color >> 8) & 0xFF;
		int fb = color & 0xFF;

		double hw = thickness * 0.5;
		int xmin = Math.min(x1, x2) - (int) (hw + 1.0);
		int xmax = Math.max(x1, x2) + (int) (hw + 1.0);
		int ymin = Math.min(y1, y2) - (int) (hw + 1.0);
		int ymax = Math.max(y1, y2) + (int) (hw + 1.0);

		if (xmin < 0) xmin = 0;
		if (ymin < 0) ymin = 0;
		if (xmax >= WIDTH) xmax = WIDTH - 1;
		if (ymax >= HEIGHT) ymax = HEIGHT - 1;

		for (int y = ymin; y <= ymax; y++) {
			for (int x = xmin; x <= xmax; x++) {
				double px = x + 0.5;
				double py = y + 0.5;
				double dist = pointToSegmentDistance(px, py, x1 + 0.5, y1 + 0.5, x2 + 0.5, y2 + 0.5);
				double alpha = coverage(dist - hw);
				if (alpha <= 0.0) {
					continue;
				}
				blend((y * WIDTH + x) * 4, fr, fg, fb, alpha);
			}
		}
	}

	public void drawArc(int cx, int cy, double radius, double startAngle, double endAngle, double thickness, int color) {
		drawArc(cx, cy, radius, startAngle, endAngle, thickness, color, false, 0xffffff);
	}

	public void drawArc(int cx, int cy, double radius, double startAngle, double endAngle, double thickness,
			int color, boolean stripe) {
		drawArc(cx, cy, radius, startAngle, endAngle, thickness, color, stripe, 0xffffff);
	}

	public void drawArc(int cx, int cy, double radius, double startAngle, double endAngle, double thickness,
			int color, boolean stripe, int stripeColor) {
		int fr = (color >> 16) & 0xFF;
		int fg = (color >> 8) & 0xFF;
		int fb = color & 0xFF;
		double hw = thickness * 0.5;
		int margin = (int) (hw + 1.0);
		int xmin = (int) (cx - radius - margin);
		int xmax = (int) (cx + radius + margin);
		int ymin = (int) (cy - radius - margin);
		int ymax = (int) (cy + radius + margin);
		if (xmin < 0) xmin = 0;
		if (ymin < 0) ymin = 0;
		if (xmax >= WIDTH) xmax = WIDTH - 1;
		if (ymax >= HEIGHT) ymax = HEIGHT - 1;
		for (int y = ymin; y <= ymax; y++) {
			for (int x = xmin; x <= xmax; x++) {
				double px = x + 0.5;
				double py = y + 0.5;
				double dist = pointToArcDistance(px, py, cx + 0.5, cy + 0.5, radius, startAngle, endAngle, hw);
				double alpha = coverage(dist - hw);
				if (alpha <= 0.0) {
					continue;
				}
				blend((y * WIDTH + x) * 4, fr, fg, fb, alpha);
			}
		}

		if (stripe) {
			double halfWidth = thickness / 2.0;
			double innerR = radius - halfWidth;
			double outerR = radius + halfWidth;
			double stripeSpacingDeg = 6.0;
			double gridOrigin = -90.0;
			double n0 = Math.ceil((startAngle - gridOrigin) / stripeSpacingDeg);
			double a = gridOrigin + n0 * stripeSpacingDeg;
			double cosR = Math.cos(Math.toRadians(45.0));
			double sinR = Math.sin(Math.toRadians(45.0));
			while (a <= endAngle) {
				double rad = Math.toRadians(a);
				double xInner = cx + innerR * Math.cos(rad);
				double yInner = cy + innerR * Math.sin(rad);
				double xOuter = cx + outerR * Math.cos(rad);
				double yOuter = cy + outerR * Math.sin(rad);
				double mx = (xInner + xOuter) / 2.0;
				double my = (yInner + yOuter) / 2.0;
				double dxInner = xInner - mx;
				double dyInner = yInner - my;
				double xInnerRot = mx + dxInner * cosR - dyInner * sinR;
				double yInnerRot = my + dxInner * sinR + dyInner * cosR;
				double dxOuter = xOuter - mx;
				double dyOuter = yOuter - my;
				double xOuterRot = mx + dxOuter * cosR - dyOuter * sinR;
				double yOuterRot = my + dxOuter * sinR + dyOuter * cosR;
				drawLine((int) xInnerRot, (int) yInnerRot, (int) xOuterRot, (int) yOuterRot, 1, stripeColor);
				a += stripeSpacingDeg;
			}
		}
	}

	public void drawCircle(int cx, int cy, double radius, double thickness, int color) {
		drawArc(cx, cy, radius, 0.0, 360.0, thickness, color);
	}

	public void fillCircle(int cx, int cy, double radius, int color) {
		int fr = (color >> 16) & 0xFF;
		int fg = (color >> 8) & 0xFF;
		int fb = color & 0xFF;

		int margin = 1;
		int xmin = (int) (cx - radius - margin);
		int xmax = (int) (cx + radius + margin);
		int ymin = (int) (cy - radius - margin);
		int ymax = (int) (cy + radius + margin);
		if (xmin < 0) xmin = 0;
		if (ymin < 0) ymin = 0;
		if (xmax >= WIDTH) xmax = WIDTH - 1;
		if (ymax >= HEIGHT) ymax = HEIGHT - 1;

		for (int y = ymin; y <= ymax; y++) {
			for (int x = xmin; x <= xmax; x++) {
				double px = x + 0.5 - (cx + 0.5);
				double py = y + 0.5 - (cy + 0.5);
				double dist = Math.sqrt(px * px + py * py);
				double alpha = coverage(dist - radius);
				if (alpha <= 0.0) {
					continue;
				}
				blend((y * WIDTH + x) * 4, fr, fg, fb, alpha);
			}
		}
	}

	// Draws one glyph centred on a point at (radius, angle) around (cx, cy), rotated by rotation degrees
	public void drawCurvedChar(int cx, int cy, double radius, double angle, String ch, Typeface typeface,
			int fontSize, double rotation) {
		drawCurvedChar(cx, cy, radius, angle, ch, typeface, fontSize, rotation, Color.BLACK);
	}

	// color is ARGB
	public void drawCurvedChar(int cx, int cy, double radius, double angle, String ch, Typeface typeface,
			int fontSize, double rotation, int color) {
		int scale = 4;
		int bigSize = (int) (fontSize * scale);
		String cacheKey = System.identityHashCode(typeface) + ":" + bigSize + ":" + color;
		Paint paint = paintCache.get(cacheKey);
		if (paint == null) {
			paint = new Paint(Paint.ANTI_ALIAS_FLAG);
			paint.setTypeface(typeface);
			paint.setTextSize(bigSize);
			paint.setColor(color);
			paintCache.put(cacheKey, paint);
		}

		int side = bigSize * 2;
		Bitmap img = Bitmap.createBitmap(side, side, Bitmap.Config.ARGB_8888);
		Canvas canvas = new Canvas(img);
		Rect bbox = new Rect();
		paint.getTextBounds(ch, 0, ch.length(), bbox);
		float tx = (side - bbox.width()) / 2 - bbox.left;
		float ty = (side - bbox.height()) / 2 - bbox.top;

		// Canvas rotates clockwise, the Python (PIL) rotation was counter-clockwise
		double rotAngle = -angle - 90.0 + rotation;
		canvas.save();
		canvas.rotate((float) -rotAngle, side / 2f, side / 2f);
		canvas.drawText(ch, tx, ty, paint);
		canvas.restore();

		int finalSize = (int) (fontSize * 1.2);
		Bitmap resized = Bitmap.createScaledBitmap(img, finalSize, finalSize, true);
		int[] pixels = new int[finalSize * finalSize];
		resized.getPixels(pixels, 0, finalSize, 0, 0, finalSize, finalSize);
		if (resized != img) {
			resized.recycle();
		}
		img.recycle();

		int x = (int) (cx + radius * Math.cos(Math.toRadians(angle)) - finalSize / 2);
		int y = (int) (cy + radius * Math.sin(Math.toRadians(angle)) - finalSize / 2);
		for (int iy = 0; iy < finalSize; iy++) {
			for (int ix = 0; ix < finalSize; ix++) {
				int argb = pixels[iy * finalSize + ix];
				int a = (argb >>> 24) & 0xFF;
				if (a == 0) {
					continue;
				}
				int px = x + ix;
				int py = y + iy;
				if (px >= 0 && px < WIDTH && py >= 0 && py < HEIGHT) {
					blend((py * WIDTH + px) * 4, (argb >> 16) & 0xFF, (argb >> 8) & 0xFF, argb & 0xFF, a / 255.0);
				}
			}
		}
	}
}
