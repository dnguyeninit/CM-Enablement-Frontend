import ResourceBundleUtil from "@jangaroo/runtime/l10n/ResourceBundleUtil";
import PersonalizationContext_properties from "./PersonalizationContext_properties";

/**
 * Contains localization properties used for user-context data. Context names, their properties, and the respective
 * values can be localized by using the patterns
 *
 *   <actualContextName>=<localizedContextName> for context names
 *   <actualContextName>_<actualPropertyName>=<localizedPropertyName> for property keys
 *   <actualContextName>_<actualPropertyName>_<actualPropertyValue>=<localizedPropertyValue> for property values
 *
 * All non-word characters, that is everything but (A-Z, a-z, 0-9, or _), are removed from the actual names before
 * a property look-up is performed.
 *
 * If a localization of a context, property, or value is missing, the actual value is used.
 *
 * @see PersonalizationContext_properties#INSTANCE
 */
ResourceBundleUtil.override(PersonalizationContext_properties, {
  p13n_context_system: "Systemeigenschaften",
  p13n_context_system_timeOfDay: "Tageszeit",
  p13n_context_system_date: "Datum",
  p13n_context_system_dayOfWeek: "Wochentag",
  p13n_context_system_dayOfWeek_1: "Sonntag",
  p13n_context_system_dayOfWeek_2: "Montag",
  p13n_context_system_dayOfWeek_3: "Dienstag",
  p13n_context_system_dayOfWeek_4: "Mittwoch",
  p13n_context_system_dayOfWeek_5: "Donnerstag",
  p13n_context_system_dayOfWeek_6: "Freitag",
  p13n_context_system_dayOfWeek_7: "Samstag",
  p13n_context_system_dateTime: "Datum und Uhrzeit",
  p13n_context_keyword: "Interessen",
  p13n_context_personal: "Persönliche Informationen",
  p13n_context_personal_dateofbirth: "Geburtsdatum",
  p13n_context_personal_street: "Adresse",
  p13n_context_personal_additionaladdressinformation: "Adresszusatz",
  p13n_context_personal_emailaddress: "E-Mail",
  p13n_context_personal_role: "Rolle",
  p13n_context_personal_website: "Website",
  p13n_context_personal_phonenumber: "Telefonnummer",
  p13n_context_personal_postalcode: "Postleitzahl",
  p13n_context_personal_timezone: "Zeitzone",
  p13n_context_personal_title: "Titel",
  p13n_context_personal_givenname: "Vorname",
  p13n_context_personal_nickname: "Spitzname",
  p13n_context_personal_familyname: "Familienname",
  p13n_context_personal_fullname: "Name",
  p13n_context_personal_skypename: "Skype",
  p13n_context_personal_cellphonenumber: "Handy",
  p13n_context_personal_locality: "Ort",
  p13n_context_personal_organization: "Unternehmen",
  p13n_context_personal_country: "Land",
  p13n_context_explicit_social: "Elastic Social",
  p13n_context_explicit_adaptive: "Adaptive Websites",
  p13n_context_explicit_mobile: "Mobile Delivery",
  p13n_context_explicit_multitouch: "Multi Touchpoint",
  p13n_context_referrer: "Referrer Information",
  p13n_context_referrer_emptyText: "Die URL von der aus die Kundenpersona auf Ihre Blueprint Webseite geleitet wurde.",
  p13n_context_referrer_query: "Suchanfrage",
  p13n_context_referrer_searchengine: "Suchmaschine",
  p13n_context_segment: "Segmente",
  p13n_context_example: "Beispiel-Kontext",
  p13n_context_example_boolean: "Boolsche Eigenschaft",
  p13n_context_testuser_profile_socialsoftware: "Elastic Social",
  p13n_context_testuser_profile_givenname: "Vorname",
  p13n_context_testuser_profile_givenname_emptyText: "Geben Sie hier den Vornamen der Kundenpersona ein.",
  p13n_context_testuser_profile_name: "Name",
  p13n_context_testuser_profile_name_emptyText: "Geben Sie hier den Nachnamen der Kundenpersona ein.",
  p13n_context_testuser_profile_birthday: "Geburtsdatum",
  p13n_context_testuser_profile_email: "E-Mail",
  p13n_context_testuser_profile_systemproperties: "Systemeigenschaften",
  p13n_context_testuser_profile_date_and_time: "Datum und Uhrzeit",
  p13n_context_testuser_profile_interests: "Interessen",
  p13n_context_testuser_profile_addinterests: "+ Interessenfeld hinzufügen",
  p13n_context_testuser_profile_withvalue: "hat den Wert",
  p13n_context_testuser_profile_image: "Bild",
  p13n_context_system_dateandtime: "Datum und Uhrzeit",
  p13n_context_personal_dateandtime: "Datum und Uhrzeit",
  p13n_context_es_check: "Social",
  p13n_context_es_check_numberOfComments: "Anzahl Kommentare",
  p13n_context_es_check_userLoggedIn: "Benutzer\\ eingeloggt",
  p13n_context_es_check_numberOfRatings: "Anzahl von Bewertungen",
  p13n_context_es_check_numberOfLikes: "Anzahl von Likes",
  p13n_context_explicit_numberOfExplicitInterests: "Anzahl der expliziten Interessen",
  p13n_context_last_visited_pagesVisited: "Anzahl der besuchten Seiten",
  p13n_context_last_visited: "Zuletzt angesehen",
  p13n_context_location_city: "Stadt",
  p13n_context_gender: "Geschlecht",
});
