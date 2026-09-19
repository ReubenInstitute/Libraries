HEBREW_NUMERALS = {
	'specials_suffix': {
		15: 'טו', 16: 'טז',
	},
	'numerals': {
		1: 'א', 2: 'ב', 3: 'ג', 4: 'ד', 5: 'ה', 6: 'ו', 7: 'ז', 8: 'ח', 9: 'ט',
		10: 'י', 20: 'כ', 30: 'ל', 40: 'מ', 50: 'נ', 60: 'ס', 70: 'ע', 80: 'פ', 90: 'צ',
		100: 'ק', 200: 'ר', 300: 'ש', 400: 'ת', 500: 'תק', 600: 'תר', 700: 'תש', 800: 'תת', 900: 'תתק',
	},
	'sofit': {
		500: 'ך', 600: 'ם', 700: 'ן', 800: 'ף', 900: 'ץ',
	},
	'separators': {
		'geresh': '׳',
		'gershayim': '״',
	}
}

REVERSE_MAP_DICT = {v: k for k, v in HEBREW_NUMERALS['numerals'].items()}
REVERSE_SOFIT_MAP = dict(REVERSE_MAP_DICT)
REVERSE_SOFIT_MAP.update({v: k for k, v in HEBREW_NUMERALS['sofit'].items()})

def _add_gershayim(s):
	if len(s) == 1:
		s += HEBREW_NUMERALS['separators']['geresh']
	else:
		s = ''.join([s[:-1], HEBREW_NUMERALS['separators']['gershayim'], s[-1:]])
	return s

def _digit_letters(value, sofit_notation):
	if sofit_notation and value in HEBREW_NUMERALS['sofit']:
		return HEBREW_NUMERALS['sofit'][value]
	return HEBREW_NUMERALS['numerals'][value]

def _convert_less_than_1000(num, gershayim=True, sofit_notation=False):
	if num <= 0 or num >= 1000:
		return ""
	base = num % 100
	hundreds = num - base
	if base in HEBREW_NUMERALS['specials_suffix']:
		suffix = HEBREW_NUMERALS['specials_suffix'][base]
		if hundreds == 0:
			remainder_str = suffix
		else:
			remainder_str = _digit_letters(hundreds, sofit_notation) + suffix
	else:
		parts = []
		rest_str = str(num)
		while rest_str:
			digit = int(rest_str[0])
			rest_str = rest_str[1:]
			if digit == 0:
				continue
			power = 10 ** len(rest_str)
			parts.append(_digit_letters(power * digit, sofit_notation))
		remainder_str = ''.join(parts)
	if gershayim:
		return _add_gershayim(remainder_str)
	return remainder_str

def hebrew_number(num, sofit_notation=False):
	return int_to_gematria(num, sofit_notation=sofit_notation)

def hebrew_fancy_number(num, sofit_notation=False):
	return int_to_gematria(num, gershayim=True, sofit_notation=sofit_notation)


def int_to_gematria(num, gershayim=False, book_notation=False, sofit_notation=False):
	if book_notation and sofit_notation:
		raise ValueError("book_notation and sofit_notation cannot both be used: book_notation never produces a value in sofit's 500-900 range.")
	if not isinstance(num, int) or num < 1:
		return ""
	if book_notation:
		tav_count = num // 400
		remainder = num % 400
		result_parts = []
		if tav_count > 0:
			result_parts.append('ת' * tav_count)
		if remainder > 0:
			result_parts.append(_convert_less_than_1000(remainder, gershayim=False))
		final_str = ''.join(result_parts)
		if gershayim and final_str:
			return _add_gershayim(final_str)
		return final_str
	else:
		thousands = num // 1000
		remainder = num % 1000
		result_parts = []
		if thousands > 0:
			thousands_str = int_to_gematria(thousands, gershayim=False, sofit_notation=sofit_notation)
			result_parts.append(thousands_str + HEBREW_NUMERALS['separators']['geresh'])
		if remainder > 0:
			result_parts.append(_convert_less_than_1000(remainder, gershayim, sofit_notation))
		return ''.join(result_parts)

def gematria_to_int(gematria_str, book_notation=False, sofit_notation=False):
	if book_notation and sofit_notation:
		raise ValueError("book_notation and sofit_notation cannot both be used: book_notation never produces a value in sofit's 500-900 range.")
	if not isinstance(gematria_str, str) or not gematria_str:
		return 0
	reverse_map = REVERSE_SOFIT_MAP if sofit_notation else REVERSE_MAP_DICT
	clean_str = gematria_str.replace(HEBREW_NUMERALS['separators']['gershayim'], '')
	if book_notation:
		tav_count = 0
		for char in clean_str:
			if char == 'ת':
				tav_count += 1
			else:
				break
		tav_value = tav_count * 400
		remainder_str = clean_str[tav_count:]
		remainder_value = 0
		for char in remainder_str:
			if char != HEBREW_NUMERALS['separators']['geresh']:
				remainder_value += reverse_map.get(char, 0)
		return tav_value + remainder_value
	else:
		if len(clean_str) == 2 and clean_str.endswith(HEBREW_NUMERALS['separators']['geresh']):
			return reverse_map.get(clean_str[0], 0)
		res = 0
		parts = clean_str.split(HEBREW_NUMERALS['separators']['geresh'])
		if len(parts) > 1 and parts[0]:
			thousands_str = parts[0]
			thousands_val = 0
			for char in thousands_str:
				thousands_val += reverse_map.get(char, 0)
			res += thousands_val * 1000
			remainder_str = parts[1]
		else:
			remainder_str = parts[0]
		for char in remainder_str:
			res += reverse_map.get(char, 0)
		return res
