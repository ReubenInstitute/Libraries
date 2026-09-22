import unicodedata
import re
import regex

ALEF = chr(0x05D0)
BET = chr(0x05D1)
GIMEL = chr(0x05D2)
DALET = chr(0x05D3)
VAV = chr(0x05D5)
ZAYIN = chr(0x05D6)
TET = chr(0x05D8)
YOD = chr(0x05D9)
KAF = chr(0x05DB)
LAMED = chr(0x05DC)
MEM = chr(0x05DE)
NUN = chr(0x05E0)
SAMEKH = chr(0x05E1)
PE = chr(0x05E4)
TSADI = chr(0x05E6)
QOF = chr(0x05E7)
RESH = chr(0x05E8)
BASESHIN = chr(0x05E9)
SHIN = BASESHIN + chr(0x05C1)
SIN = BASESHIN + chr(0x05C2)
TAV = chr(0x05EA)

WIDE_ALEF = chr(0xFB21)
WIDE_DALET = chr(0xFB22)
WIDE_HE = chr(0xFB23)
WIDE_KAF = chr(0xFB24)
WIDE_LAMED = chr(0xFB25)
WIDE_RESH = chr(0xFB27)
WIDE_TAV = chr(0xFB28)

HE = chr(0x05D4)
HET = chr(0x05D7)
AYIN = chr(0x05E2)

FINAL_KAF = chr(0x05DA)
FINAL_MEM = chr(0x05DD)
FINAL_NUN = chr(0x05DF)
FINAL_PE = chr(0x05E3)
FINAL_TSADI = chr(0x05E5)
WIDE_FINAL_MEM = chr(0xFB26)

SHEVA = chr(0x05B0)
PATAH = chr(0x05B7)
QAMATS = chr(0x05B8)
QAMATS_KATAN = chr(0x05C7)
TSERE = chr(0x05B5)
SEGOL = chr(0x05B6)
HIRIQ = chr(0x05B4)
HOLAM = chr(0x05B9)
HOLAM_HASER = chr(0x05BA)
QUBUTS = chr(0x05BB)

HATAF_SEGOL = chr(0x05B1)
HATAF_PATAH = chr(0x05B2)
HATAF_QAMATS = chr(0x05B3)

DAGESH = chr(0x05BC)
RAFE = chr(0x05BF)

ETNAHTA = chr(0x0591)
SEGOL_ACCENT = chr(0x0592)
SHALSHELET = chr(0x0593)
ZAQEF_QATAN = chr(0x0594)
ZAQEF_GADOL = chr(0x0595)
TIPEHA = chr(0x0596)
REVIA = chr(0x0597)
ZARQA = chr(0x0598)
PASHTA = chr(0x0599)
YETIV = chr(0x059A)
TEVIR = chr(0x059B)
GERESH_CANTILLATION = chr(0x059C)
GERESH_MUQDAM = chr(0x059D)
GERSHAYIM_CANTILLATION = chr(0x059E)
QARNEY_PARA = chr(0x059F)
TELISHA_GEDOLA = chr(0x05A0)
PAZER = chr(0x05A1)
QADMA = chr(0x05A8)
TELISHA_QETANA = chr(0x05A9)
YERAH_BEN_YOMO = chr(0x05AA)
OLE = chr(0x05AB)
ILUY = chr(0x05AC)
DEHI = chr(0x05AD)
ZINOR = chr(0x05AE)

MUNAH = chr(0x05A2)
MAHAPAKH = chr(0x05A3)
MERKHA = chr(0x05A4)
MERKHA_KEFULA = chr(0x05A5)
DARGA = chr(0x05A6)
QADMA_OLD = chr(0x05A7)
METEG = chr(0x05BD)

UPPER_DOT = chr(0x05C4)
LOWER_DOT = chr(0x05C5)
NUN_HAFUKHA = chr(0x05C6)
MASORA_CIRCLE = chr(0x05AF)

SOF_PASUK = chr(0x05C3)
PASEQ = chr(0x05C0)
MAQAF = chr(0x05BE)
GERESH = chr(0x05F3)
GERSHAYIM = chr(0x05F4)

YIDDISH_VAV_VAV = chr(0x05F0)
YIDDISH_VAV_YOD = chr(0x05F1)
YIDDISH_YOD_YOD = chr(0x05F2)
ALEF_LAMED = chr(0xFB4F)
YIDDISH_PASHTA = chr(0xFB1E)

PLAIN_LETTERS = [
	BET,
	GIMEL,
	DALET,
	VAV,
	ZAYIN,
	TET,
	YOD,
	KAF,
	LAMED,
	MEM,
	NUN,
	SAMEKH,
	PE,
	TSADI,
	QOF,
	RESH,
	SHIN,
	SIN,
	TAV,
	WIDE_DALET,
	WIDE_KAF,
	WIDE_LAMED,
	WIDE_RESH,
	WIDE_TAV
]

GUTTURALS = [
	ALEF,
	HE,
	HET,
	AYIN,
	WIDE_ALEF,
	WIDE_HE
]

FINALS = [
	FINAL_KAF,
	FINAL_MEM,
	FINAL_NUN,
	FINAL_PE,
	FINAL_TSADI,
	WIDE_FINAL_MEM
]

ALL_LETTERS = PLAIN_LETTERS + GUTTURALS + FINALS

# Single-character form of ALL_LETTERS: SHIN/SIN in ALL_LETTERS are each a
# base letter + combining dot (two chars), which never matches a bare "ש"
# (no dot) when filtering text one character at a time. Taking just the
# leading/identifying character of each entry gives the right per-char set.
ALL_LETTER_CHARS = frozenset(letter[0] for letter in ALL_LETTERS)

DIACRITICS = [
	SHEVA,
	PATAH,
	QAMATS,
	QAMATS_KATAN,
	TSERE,
	SEGOL,
	HIRIQ,
	HOLAM,
	HOLAM_HASER,
	QUBUTS
]

HATAFS = [
	HATAF_SEGOL,
	HATAF_PATAH,
	HATAF_QAMATS
]

UPPER_CANTILLATIONS = [
	ETNAHTA,
	SEGOL_ACCENT,
	SHALSHELET,
	ZAQEF_QATAN,
	ZAQEF_GADOL,
	TIPEHA,
	REVIA,
	ZARQA,
	PASHTA,
	YETIV,
	TEVIR,
	GERESH_CANTILLATION,
	GERESH_MUQDAM,
	GERSHAYIM_CANTILLATION,
	QARNEY_PARA,
	TELISHA_GEDOLA,
	PAZER,
	QADMA,
	TELISHA_QETANA,
	YERAH_BEN_YOMO,
	OLE,
	ILUY,
	DEHI,
	ZINOR
]

LOWER_CANTILLATIONS = [
	MUNAH,
	MAHAPAKH,
	MERKHA,
	MERKHA_KEFULA,
	DARGA,
	QADMA_OLD,
	METEG
]

CANTILLATIONS = UPPER_CANTILLATIONS + LOWER_CANTILLATIONS

MARKS = [
	UPPER_DOT,
	LOWER_DOT,
	NUN_HAFUKHA,
	MASORA_CIRCLE
]

PUNCTUATION = [
	SOF_PASUK,
	PASEQ,
	MAQAF,
	GERESH,
	GERSHAYIM
]

EXTRA = [
	YIDDISH_VAV_VAV,
	YIDDISH_VAV_YOD,
	YIDDISH_YOD_YOD,
	ALEF_LAMED,
	YIDDISH_PASHTA
]

DAGESH_HAZZAK_LETTERS = {
	ZAYIN,
	TET,
	YOD,
	LAMED,
	MEM,
	NUN,
	SAMEKH,
	TSADI,
	QOF,
	SHIN[0],
	FINAL_KAF,
}

class Char:
	def __init__(self, char):
		self.char = char

class Letter(Char):
	@property
	def base_letter(self):
		if self.char in [FINAL_KAF, FINAL_MEM, FINAL_NUN, FINAL_PE, FINAL_TSADI]:
			return {FINAL_KAF: KAF, FINAL_MEM: MEM, FINAL_NUN: NUN, FINAL_PE: PE, FINAL_TSADI: TSADI}[self.char]
		if self.char in [WIDE_ALEF, WIDE_DALET, WIDE_HE, WIDE_KAF, WIDE_LAMED, WIDE_FINAL_MEM, WIDE_RESH, WIDE_TAV]:
			return {WIDE_ALEF: ALEF, WIDE_DALET: DALET, WIDE_HE: HE, WIDE_KAF: KAF, WIDE_LAMED: LAMED, WIDE_FINAL_MEM: MEM, WIDE_RESH: RESH, WIDE_TAV: TAV}[self.char]
		if self.char == SIN or self.char == SHIN:
			return SHIN[0]
		return self.char

	def is_final(self):
		return self.char in [FINAL_KAF, FINAL_MEM, FINAL_NUN, FINAL_PE, FINAL_TSADI, WIDE_FINAL_MEM]

	def is_wide(self):
		return self.char in [WIDE_ALEF, WIDE_DALET, WIDE_HE, WIDE_KAF, WIDE_LAMED, WIDE_FINAL_MEM, WIDE_RESH, WIDE_TAV]

	def is_begedkefet(self):
		return self.base_letter in [BET, GIMEL, DALET, KAF, PE, TAV]

	def is_guttural(self):
		return self.base_letter in [ALEF, HE, HET, AYIN]

	def is_emphatic(self):
		return self.base_letter in [TET, TSADI, QOF]

	def can_have_dagesh(self):
		if self.is_guttural():
			return False
		if self.base_letter == RESH:
			return False
		if self.is_final():
			return self.base_letter in [KAF, PE]
		return True

	def can_have_rafe(self):
		return self.is_begedkefet()

	def has_final_form(self):
		return self.base_letter in [KAF, MEM, NUN, PE, TSADI]



class Diacritic(Char):
	def is_vowel(self):
		return self.char in [SHEVA, PATAH, QAMATS, QAMATS_KATAN, TSERE, SEGOL, HIRIQ, HOLAM, HOLAM_HASER, QUBUTS]

	def is_hataf(self):
		return self.char in [HATAF_SEGOL, HATAF_PATAH, HATAF_QAMATS]

	def is_dagesh(self):
		return self.char in [DAGESH, RAFE]

	def is_rafe(self):
		return self.char == RAFE

	def is_shin_dot(self):
		return self.char == SHIN[1]

	def is_sin_dot(self):
		return self.char == SIN[1]

	def is_above(self):
		return self.char in [HIRIQ, TSERE, SEGOL, PATAH, QAMATS, HOLAM, HOLAM_HASER, QUBUTS, SHIN[1], SIN[1]]

	def is_below(self):
		return self.char in [SHEVA, HATAF_SEGOL, HATAF_PATAH, HATAF_QAMATS, DAGESH, RAFE]

	def is_inside(self):
		return False

	def is_short_vowel(self):
		return self.char in [SHEVA, HATAF_SEGOL, HATAF_PATAH, HATAF_QAMATS, PATAH, SEGOL]

	def is_long_vowel(self):
		return self.char in [QAMATS, QAMATS_KATAN, TSERE, HOLAM, HOLAM_HASER, QUBUTS]

	def is_reduced_vowel(self):
		return self.is_hataf()

	def can_combine_with_guttural(self):
		return True

	def can_combine_with_final(self):
		return not self.is_dagesh() or self.char == DAGESH

letters = [Letter(char) for char in PLAIN_LETTERS]
gutturals = [Letter(char) for char in GUTTURALS]
finals = [Letter(char) for char in FINALS]
diacritics = [Diacritic(char) for char in DIACRITICS]
hatafs = [Diacritic(char) for char in HATAFS]

normal_letters = [letter for letter in letters if not letter.is_wide()]
wide_letters = [letter for letter in letters if letter.is_wide()]
normal_gutturals = [guttural for guttural in gutturals if not guttural.is_wide()]
wide_gutturals = [guttural for guttural in gutturals if guttural.is_wide()]
normal_finals = [final for final in finals if not final.is_wide()]
wide_finals = [final for final in finals if final.is_wide()]

COMBINING_ORDER = [
	'ׁ', 'ׂ',
	'ּ', 'ֿ',
	*DIACRITICS,
	*HATAFS,
	*UPPER_CANTILLATIONS,
	*LOWER_CANTILLATIONS,
	*MARKS
]

def normalize(text):
	text = unicodedata.normalize('NFC', text)
	pattern = r'\P{M}\p{M}*'
	blocks = regex.findall(pattern, text, regex.UNICODE)
	normalized_blocks = []
	for block in blocks:
		if len(block) == 1:
			normalized_blocks.append(block)
		else:
			base = block[0]
			modifiers = list(block[1:])
			modifiers.sort(key=lambda c: COMBINING_ORDER.index(c) if c in COMBINING_ORDER else len(COMBINING_ORDER))
			normalized_blocks.append(base + ''.join(modifiers))
	return ''.join(normalized_blocks)

def add_dagesh_hazzak(text):
	text = normalize(text)
	blocks = regex.findall(r'\P{M}\p{M}*', text, regex.UNICODE)
	result = []
	for block in blocks:
		base = block[0]
		modifiers = list(block[1:])
		base_normal = Letter(base).base_letter
		if base_normal in DAGESH_HAZZAK_LETTERS:
			dagesh_count = modifiers.count(DAGESH)
			if dagesh_count == 1:
				modifiers.append(DAGESH)
				modifiers.sort(key=lambda c: COMBINING_ORDER.index(c) if c in COMBINING_ORDER else len(COMBINING_ORDER))
		result.append(base + ''.join(modifiers))
	return ''.join(result)

def add_sheva_na(text):
	text = normalize(text)
	blocks = regex.findall(r'\P{M}\p{M}*', text, regex.UNICODE)
	result = []
	previous_base = None
	for block in blocks:
		base = block[0]
		modifiers = list(block[1:])
		if base in ALL_LETTER_CHARS and previous_base not in ALL_LETTER_CHARS:
			sheva_count = modifiers.count(SHEVA)
			if sheva_count == 1:
				modifiers.append(SHEVA)
				modifiers.sort(key=lambda c: COMBINING_ORDER.index(c) if c in COMBINING_ORDER else len(COMBINING_ORDER))
		result.append(base + ''.join(modifiers))
		previous_base = base
	return ''.join(result)

def strip_punctuation(text):
	text = re.sub(r'[\,\.\!\?\:\;\-–—…\'\"]', '', text)
	text = re.sub(r" +", " ", text)
	return text.strip()

def strip_cantillation(text, strip_meteg=True):
	if strip_meteg:
		text = text.replace(METEG, "")
	non_meteg = ''.join(c for c in CANTILLATIONS if c != METEG)
	return re.sub('[' + non_meteg + ']', '', text)

def strip_diacritics(text, strip_rafe=True):
	niqqud = ''.join(DIACRITICS + HATAFS + [DAGESH, SHIN[1], SIN[1]])
	text = re.sub('[' + niqqud + ']', '', text)
	if strip_rafe:
		text = text.replace(RAFE, '')
	return text

def strip_hebrew_punctuation(text, strip_maqaf=True, strip_masora=True):
	text = text.replace(PASEQ, '').replace(SOF_PASUK, '')
	if strip_maqaf:
		text = text.replace(MAQAF, ' ')
	if strip_masora:
		text = text.replace(UPPER_DOT, '').replace(LOWER_DOT, '')
	return text

def strip_yy(text):
	return text.replace('יְיָ', 'יי')

def strip_yhwh(text):
	y = YOD
	h = HE
	H = WIDE_HE
	w = VAV
	a = '([' + ''.join(CANTILLATIONS + [MASORA_CIRCLE, METEG, PASEQ, SOF_PASUK]) + ']*)'
	p = '([' + ''.join(DIACRITICS + HATAFS + [DAGESH, SHIN[1], SIN[1]]) + ']*)'
	pattern = y + p + a + h + p + a + w + p + a + h + p + a
	for match in reversed(list(re.finditer(pattern, text, re.M))):
		orig = text[match.start():match.end()]
		p1, a1, p2, a2, p3, a3, p4, a4 = match.groups()
		out = y + a1 + H + a2 + w + a3 + H + a4
		start, end = match.span()
		text = text[0:start] + out + text[end:]
	return text

def presentation(text):
	return strip_cantillation(strip_hebrew_punctuation(strip_yhwh(text)))

def raw_text(text):
	text = strip_yhwh(text)
	text = text.replace(MAQAF, ' ')
	text = ''.join(c for c in text if c in ALL_LETTER_CHARS or c == ' ')
	return re.sub(r' +', ' ', text).strip()

def transliterate(text):
	HEBREW_TO_ASCII = "ABGDHVZHTIKKLMMNNSAPPSSQRST"
	text = strip_cantillation(text)
	text = strip_diacritics(text)
	result = []
	for char in text:
		if ALEF <= char <= TAV:
			index = ord(char) - ord(ALEF)
			if index < len(HEBREW_TO_ASCII):
				result.append(HEBREW_TO_ASCII[index])
			else:
				result.append(char)
		else:
			result.append(char)
	return ''.join(result)
