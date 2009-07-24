/*
 * This file is part of JMoviedb.
 * 
 * Copyright (C) Tor Arne Lye torarnelye@gmail.com
 * 
 * JMoviedb is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 3 of the License, or
 * (at your option) any later version.
 * 
 * JMoviedb is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.greyzone.domain.movie.enumerated;


@SuppressWarnings("static-access")

/**
 * An enum containing all the languages assigned to movies at IMDb.
 * @author Tor
 *
 */
public enum Language {
	none(0, "None", "None"),
	albanian(1, "Albanian", "Albanian"),
	arabic(2, "Arabic", "Arabic"),
	bengali(3, "Bengali", "Bengali"),
	bulgarian(4, "Bulgarian", "Bulgarian"),
	cantonese(5, "Cantonese", "Cantonese"),
	catalan(6, "Catalan", "Catalan"),
	czech(7, "Czech", "Czech"),
	danish(8, "Danish", "Danish"),
	dutch(9, "Dutch", "Dutch"),
	english(10, "English", "English"),
	filipino(11, "Filipino", "Filipino"),
	finnish(12, "Finnish", "Finnish"),
	french(13, "French", "French"),
	georgian(14, "Georgian", "Georgian"),
	german(15, "German", "German"),
	greek(16, "Greek", "Greek"),
	hebrew(17, "Hebrew", "Hebrew"),
	hindi(18, "Hindi", "Hindi"),
	hungarian(19, "Hungarian", "Hungarian"),
	italian(20, "Italian", "Italian"),
	japanese(21, "Japanese", "Japanese"),
	korean(22, "Korean", "Korean"),
	malayalam(23, "Malayalam", "Malayalam"),
	mandarin(24, "Mandarin", "Mandarin"),
	marathi(25, "Marathi", "Marathi"),
	norwegian(27, "Norwegian", "Norwegian"),
	persian(28, "Persian", "Persian"),
	polish(29, "Polish", "Polish"),
	portuguese(30, "Portuguese", "Portuguese"),
	romanian(31, "Romanian", "Romanian"),
	russian(32, "Russian", "Russian"),
	serbo_croatian(33, "Serbo-Croatian", "Serbo-Croatian"),
	spanish(34, "Spanish", "Spanish"),
	swedish(35, "Swedish", "Swedish"),
	tagalog(36, "Tagalog", "Tagalog"),
	tamil(37, "Tamil", "Tamil"),
	telugu(38, "Telugu", "Telugu"),
	turkish(39, "Turkish", "Turkish"),
	aboriginal(40, "Aboriginal", "Aboriginal"),
	acholi(41, "Acholi", "Acholi"),
	ache(42, "Aché","Ach�"),
	afrikaans(43, "Afrikaans", "Afrikaans"),
	aidoukrou(44, "Aidoukrou", "Aidoukrou"),
	akan(45, "Akan", "Akan"),
	algonquin(46, "Algonquin", "Algonquin"),
	alsatian(47, "Alsatian", "Alsatian"),
	americansignlanguage(48, "AmericanSignLanguage", "AmericanSignLanguage"),
	amharic(49, "Amharic", "Amharic"),
	aragonese(50, "Aragonese", "Aragonese"),
	aramaic(51, "Aramaic", "Aramaic"),
	arapaho(52, "Arapaho", "Arapaho"),
	armenian(53, "Armenian", "Armenian"),
	assamese(54, "Assamese", "Assamese"),
	assyrianneo_aramaic(55, "AssyrianNeo-Aramaic", "AssyrianNeo-Aramaic"),
	australiansignlanguage(56, "AustralianSignLanguage", "AustralianSignLanguage"),
	awadhi(57, "Awadhi", "Awadhi"),
	aymara(58, "Aymara", "Aymara"),
	azeri(59, "Azeri", "Azeri"),
	babledialect(60, "Babledialect", "Babledialect"),
	baka(61, "Baka", "Baka"),
	bambara(62, "Bambara", "Bambara"),
	basque(63, "Basque", "Basque"),
	bassari(64, "Bassari", "Bassari"),
	belarusian(65, "Belarusian", "Belarusian"),
	bemba(66, "Bemba", "Bemba"),
	berber(67, "Berber", "Berber"),
	bhojpuri(68, "Bhojpuri", "Bhojpuri"),
	bicolano(69, "Bicolano", "Bicolano"),
	bodo(70, "Bodo", "Bodo"),
	braziliansignlanguage(71, "BrazilianSignLanguage", "BrazilianSignLanguage"),
	breton(72, "Breton", "Breton"),
	britishsignlanguage(73, "BritishSignLanguage", "BritishSignLanguage"),
	burmese(74, "Burmese", "Burmese"),
	chaozhou(75, "Chaozhou", "Chaozhou"),
	chechen(76, "Chechen", "Chechen"),
	cherokee(77, "Cherokee", "Cherokee"),
	cheyenne(78, "Cheyenne", "Cheyenne"),
	chhattisgarhi(79, "Chhattisgarhi", "Chhattisgarhi"),
	cornish(80, "Cornish", "Cornish"),
	corsican(81, "Corsican", "Corsican"),
	cree(82, "Cree", "Cree"),
	croatian(292, "Croatian", "Croatian"),
	crow(83, "Crow", "Crow"),
	dari(84, "Dari", "Dari"),
	desia(85, "Desia", "Desia"),
	dinka(86, "Dinka", "Dinka"),
	dioula(87, "Dioula", "Dioula"),
	djerma(88, "Djerma", "Djerma"),
	dogri(89, "Dogri", "Dogri"),
	dzongkha(90, "Dzongkha", "Dzongkha"),
	east_greenlandic(91, "East-Greenlandic", "East-Greenlandic"),
	esperanto(92, "Esperanto", "Esperanto"),
	estonian(93, "Estonian", "Estonian"),
	faliasch(94, "Faliasch", "Faliasch"),
	faroese(95, "Faroese", "Faroese"),
	flemish(96, "Flemish", "Flemish"),
	fon(97, "Fon", "Fon"),
	frenchsignlanguage(98, "FrenchSignLanguage", "FrenchSignLanguage"),
	frisian(99, "Frisian", "Frisian"),
	fula(100, "Fula", "Fula"),
	gaelic(101, "Gaelic", "Gaelic"),
	galician(102, "Galician", "Galician"),
	germansignlanguage(103, "GermanSignLanguage", "GermanSignLanguage"),
	grebo(104, "Grebo", "Grebo"),
	greenlandic(105, "Greenlandic", "Greenlandic"),
	guarani(106, "Guarani", "Guarani"),
	gujarati(107, "Gujarati", "Gujarati"),
	gumatj(108, "Gumatj", "Gumatj"),
	haitian(109, "Haitian", "Haitian"),
	hakka(110, "Hakka", "Hakka"),
	haryanvi(111, "Haryanvi", "Haryanvi"),
	hassanya(112, "Hassanya", "Hassanya"),
	hausa(113, "Hausa", "Hausa"),
	hawaiian(114, "Hawaiian", "Hawaiian"),
	hokkien(115, "Hokkien", "Hokkien"),
	hopi(116, "Hopi", "Hopi"),
	iban(117, "Iban", "Iban"),
	ibo(118, "Ibo", "Ibo"),
	icelandic(119, "Icelandic", "Icelandic"),
	icelandicsignlanguage(120, "IcelandicSignLanguage", "IcelandicSignLanguage"),
	indiansignlanguage(121, "IndianSignLanguage", "IndianSignLanguage"),
	indonesian(122, "Indonesian", "Indonesian"),
	inuktitut(123, "Inuktitut", "Inuktitut"),
	irishgaelic(124, "IrishGaelic", "IrishGaelic"),
	japanesesignlanguage(125, "JapaneseSignLanguage", "JapaneseSignLanguage"),
	juhoan(126, "Juhoan", "Juhoan"),
	kaado(127, "Kaado", "Kaado"),
	kabuverdianu(128, "Kabuverdianu", "Kabuverdianu"),
	kabyle(129, "Kabyle", "Kabyle"),
	kalmyk_oirat(130, "Kalmyk-Oirat", "Kalmyk-Oirat"),
	kannada(131, "Kannada", "Kannada"),
	karaja(132, "Karaja", "Karaja"),
	karbi(133, "Karbi", "Karbi"),
	kazakh(134, "Kazakh", "Kazakh"),
	khanty(135, "Khanty", "Khanty"),
	khasi(136, "Khasi", "Khasi"),
	khmer(137, "Khmer", "Khmer"),
	kikongo(138, "Kikongo", "Kikongo"),
	kinyarwanda(139, "Kinyarwanda", "Kinyarwanda"),
	kirundi(140, "Kirundi", "Kirundi"),
	klingon(141, "Klingon", "Klingon"),
	kodava(142, "Kodava", "Kodava"),
	konkani(143, "Konkani", "Konkani"),
	koreansignlanguage(144, "KoreanSignLanguage", "KoreanSignLanguage"),
	korowai(145, "Korowai", "Korowai"),
	kriolu(146, "Kriolu", "Kriolu"),
	kru(147, "Kru", "Kru"),
	kuna(148, "Kuna", "Kuna"),
	kunwinjku(149, "Kunwinjku", "Kunwinjku"),
	kurdish(150, "Kurdish", "Kurdish"),
	kwakiutl(151, "Kwakiutl", "Kwakiutl"),
	kyrgyz(152, "Kyrgyz", "Kyrgyz"),
	ladakhi(153, "Ladakhi", "Ladakhi"),
	ladino(154, "Ladino", "Ladino"),
	lao(155, "Lao", "Lao"),
	latin(156, "Latin", "Latin"),
	latvian(157, "Latvian", "Latvian"),
	lenguayorubaantigua(158, "LenguaYorubaAntigua", "LenguaYorubaAntigua"),
	letzebuergesh(159, "Letzebuergesh", "Letzebuergesh"),
	limbu(160, "Limbu", "Limbu"),
	lingala(161, "Lingala", "Lingala"),
	lithuanian(162, "Lithuanian", "Lithuanian"),
	maasai(291, "Maasai", "Maasai"),
	macedonian(163, "Macedonian", "Macedonian"),
	macro_je(164, "Macro-Jê", "Macro-Jê"),
	magahi(165, "Magahi", "Magahi"),
	maithili(166, "Maithili", "Maithili"),
	malagasy(167, "Malagasy", "Malagasy"),
	malay(168, "Malay", "Malay"),
	malecite_passamaquoddy(169, "Malecite-Passamaquoddy", "Malecite-Passamaquoddy"),
	malinka(170, "Malinka", "Malinka"),
	maltese(171, "Maltese", "Maltese"),
	manchu(172, "Manchu", "Manchu"),
	mandingo(173, "Mandingo", "Mandingo"),
	manipuri(174, "Manipuri", "Manipuri"),
	maori(175, "Maori", "Maori"),
	mapudungun(176, "Mapudungun", "Mapudungun"),
	marshallese(177, "Marshallese", "Marshallese"),
	masalit(178, "Masalit", "Masalit"),
	maya(179, "Maya", "Maya"),
	mende(180, "Mende", "Mende"),
	micmac(181, "Micmac", "Micmac"),
	middleenglish(182, "MiddleEnglish", "MiddleEnglish"),
	minnan(183, "MinNan", "MinNan"),
	mizo(184, "Mizo", "Mizo"),
	mohawk(185, "Mohawk", "Mohawk"),
	mongolian(186, "Mongolian", "Mongolian"),
	montagnais(187, "Montagnais", "Montagnais"),
	more(188, "More", "More"),
	morisyen(189, "Morisyen", "Morisyen"),
	moso(190, "Moso", "Moso"),
	nagpuri(191, "Nagpuri", "Nagpuri"),
	nahuatl(192, "Nahuatl", "Nahuatl"),
	nama(193, "Nama", "Nama"),
	navajo(194, "Navajo", "Navajo"),
	ndebele(195, "Ndebele", "Ndebele"),
	nenets(196, "Nenets", "Nenets"),
	nepali(197, "Nepali", "Nepali"),
	nisgaa(198, "Nisgaa", "Nisgaa"),
	nushi(199, "Nushi", "Nushi"),
	nyaneka(200, "Nyaneka", "Nyaneka"),
	nyanja(201, "Nyanja", "Nyanja"),
	occitan(202, "Occitan", "Occitan"),
	ojibwa(203, "Ojibwa", "Ojibwa"),
	ojihimba(204, "Ojihimba", "Ojihimba"),
	oldenglish(205, "OldEnglish", "OldEnglish"),
	oriya(206, "Oriya", "Oriya"),
	papiamento(207, "Papiamento", "Papiamento"),
	parsee(208, "Parsee", "Parsee"),
	pashtu(209, "Pashtu", "Pashtu"),
	pawnee(210, "Pawnee", "Pawnee"),
	peul(211, "Peul", "Peul"),
	polynesian(212, "Polynesian", "Polynesian"),
	provencal(213, "Provençal", "Provençal"),
	pular(214, "Pular", "Pular"),
	punjabi(215, "Punjabi", "Punjabi"),
	purepecha(216, "Purépecha", "Purépecha"),
	quechua(217, "Quechua", "Quechua"),
	quenya(218, "Quenya", "Quenya"),
	rajasthani(219, "Rajasthani", "Rajasthani"),
	rawan(220, "Rawan", "Rawan"),
	rhaeto_romanic(221, "Rhaeto-Romanic", "Rhaeto-Romanic"),
	romany(222, "Romany", "Romany"),
	rotuman(223, "Rotuman", "Rotuman"),
	russiansignlanguage(224, "RussianSignLanguage", "RussianSignLanguage"),
	ryukyuan(225, "Ryukyuan", "Ryukyuan"),
	saami(226, "Saami", "Saami"),
	samoan(227, "Samoan", "Samoan"),
	sanskrit(228, "Sanskrit", "Sanskrit"),
	sardinian(229, "Sardinian", "Sardinian"),
	scanian(230, "Scanian", "Scanian"),
	scottishgaelic(231, "ScottishGaelic", "ScottishGaelic"),
	serbian(293, "Serbian", "Serbian"),
	serere(232, "Serere", "Serere"),
	shanghainese(233, "Shanghainese", "Shanghainese"),
	shanxi(234, "Shanxi", "Shanxi"),
	shona(235, "Shona", "Shona"),
	shoshone(236, "Shoshone", "Shoshone"),
	sicilian(237, "Sicilian", "Sicilian"),
	sindarin(238, "Sindarin", "Sindarin"),
	sindhi(239, "Sindhi", "Sindhi"),
	sinhala(240, "Sinhala", "Sinhala"),
	sioux(241, "Sioux", "Sioux"),
	skawkaren(242, "SkawKaren", "SkawKaren"),
	slovak(243, "Slovak", "Slovak"),
	slovenian(244, "Slovenian", "Slovenian"),
	somali(245, "Somali", "Somali"),
	songhay(246, "Songhay", "Songhay"),
	soninke(247, "Soninke", "Soninke"),
	sotho(248, "Sotho", "Sotho"),
	sousson(249, "Sousson", "Sousson"),
	spanishsignlanguage(250, "SpanishSignLanguage", "SpanishSignLanguage"),
	sranan(251, "Sranan", "Sranan"),
	swahili(252, "Swahili", "Swahili"),
	swissgerman(253, "SwissGerman", "SwissGerman"),
	sylhetti(254, "Sylhetti", "Sylhetti"),
	taiwanese(255, "Taiwanese", "Taiwanese"),
	tajik(256, "Tajik", "Tajik"),
	tamashek(257, "Tamashek", "Tamashek"),
	tarahumara(258, "Tarahumara", "Tarahumara"),
	tatar(259, "Tatar", "Tatar"),
	teochew(260, "Teochew", "Teochew"),
	thai(261, "Thai", "Thai"),
	tibetan(262, "Tibetan", "Tibetan"),
	tigrigna(263, "Tigrigna", "Tigrigna"),
	tlingit(264, "Tlingit", "Tlingit"),
	tongan(265, "Tongan", "Tongan"),
	tsonga(266, "Tsonga", "Tsonga"),
	tswa(267, "Tswa", "Tswa"),
	tswana(268, "Tswana", "Tswana"),
	tulu(269, "Tulu", "Tulu"),
	tupi(270, "Tupi", "Tupi"),
	turkmen(271, "Turkmen", "Turkmen"),
	tuvan(272, "Tuvan", "Tuvan"),
	tzotzil(273, "Tzotzil", "Tzotzil"),
	ukrainian(274, "Ukrainian", "Ukrainian"),
	ungwatsi(275, "Ungwatsi", "Ungwatsi"),
	urdu(276, "Urdu", "Urdu"),
	uzbek(277, "Uzbek", "Uzbek"),
	valencian(278, "Valencian", "Valencian"),
	vietnamese(279, "Vietnamese", "Vietnamese"),
	visayan(280, "Visayan", "Visayan"),
	washoe(281, "Washoe", "Washoe"),
	welsh(282, "Welsh", "Welsh"),
	wolof(283, "Wolof", "Wolof"),
	xhosa(284, "Xhosa", "Xhosa"),
	xitewa(285, "Xitewa", "Xitewa"),
	yakut(286, "Yakut", "Yakut"),
	yapese(287, "Yapese", "Yapese"),
	yiddish(288, "Yiddish", "Yiddish"),
	yoruba(289, "Yoruba", "Yoruba"),
	zulu(290, "Zulu", "Zulu");
	
	
	private int id;
	private String imdbID;
	private String name;
	
	/**
	 * Constructor.
	 * @param id a numerical ID, used for storage in the SQL database
	 * @param imdbID IMDb's ID for this language
	 * @param name the name of this language
	 */
	Language(int id, String imdbID, String name) {
		this.id = id;
		this.imdbID = imdbID;
		this.name = name;
	}
	
	public String getImdbID() {
		return imdbID;
	}
	
	public int getID() {
		return id;
	}

	public String getName() {
		return name;
	}
	
	/**
	 * Converts a string to a Language enum, for example &quot;English&quot; to Language.English 
	 * @param string the string to be converted
	 * @return a Language enum, or null if there was no match
	 */
	public static Language StringToEnum(String string) {
		if(string == null) {
			return null;
		}
		for(Language l : Language.values())
			if(string.toLowerCase().equals(l.getImdbID().toLowerCase()))
				return l;
		return null;
	}
	
	/**
	 * Looks up the correct enum value from an int ID
	 * @param i the ID to look up
	 * @return the associated enum
	 */
	public static Language intToEnum(int id) {
		for(Language l : Language.values())
			if(id == l.getID())
				return l;
			System.out.println("Unrecognised language ID: " + id);
		return null;
	}
	
	public static String[] getStringArray() {
		String[] strings = new String[Language.values().length];
		for(int i = 0; i < Language.values().length; i++)
			strings[i] = Language.values()[i].getImdbID();
		return strings;
	}
}
