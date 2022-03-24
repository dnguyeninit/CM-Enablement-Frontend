
/**
 * Interface values for ResourceBundle "Validation".
 * @see Validation_properties#INSTANCE
 */
interface Validation_properties {

/**
 *Custom Validator messages
 */
  Validator_self_referring_text: string;
  Validator_channel_loop_text: string;
  Validator_duplicate_segment_text: string;
  Validator_duplicate_root_segment_text: string;
  Validator_no_context_text: string;
  Validator_not_in_navigation_text: string;
  Validator_LengthValidator_text: string;
  Validator_duplicate_referrer_text: string;
  Validator_NotEmptyMarkupValidator_text: string;
  ContentValidator_CMChannel_segment_RegExpValidator_text: string;
  Validator_Abstract_Code_data_URL_property_must_be_set_text: string;
  Validator_Abstract_Code_code_property_must_be_set_text: string;
  ContentValidator_CMVideo_data_atLeastOneNotEmpty_text: string;
  ContentValidator_CMVideo_data_exactlyOneMustBeSet_text: string;
  Validator_UniqueInSiteStringValidator_text: string;
  Validator_UniqueStringValidator_text: string;
  Validator_no_cmpicture_text: string;
  CMNavigation_hidden_text: string;
  ValidationStatus_not_valid_anymore: string;
  ValidationStatus_will_be_active: string;
  Validator_validFrom_is_after_validTo_text: string;
  Validator_validFrom_equals_validTo_text: string;
  Validator_placement_visibleFrom_is_after_visibleTo_text: string;
  Validator_placement_visibleFrom_equals_visibleTo_text: string;
  Validator_placement_visibility_not_within_validity_text: string;
}

/**
 * Singleton for the current user Locale's instance of ResourceBundle "Validation".
 * @see Validation_properties
 */
const Validation_properties: Validation_properties = {
  Validator_self_referring_text: "The link list contains a link to itself.",
  Validator_channel_loop_text: "The page hierarchy contains a loop.",
  Validator_duplicate_segment_text: "This URL segment already exists with the same navigation parent in '{0}'.",
  Validator_duplicate_root_segment_text: "This URL root segment is already used in '{0}' of site '{1} - {2}'.",
  Validator_no_context_text: "This document has no navigation context.",
  Validator_not_in_navigation_text: "This page is not part of the navigation.",
  Validator_LengthValidator_text: "The length of the text value is too large.",
  Validator_duplicate_referrer_text: "The page is linked by more than one page.",
  Validator_NotEmptyMarkupValidator_text: "This field must not be empty.",
  ContentValidator_CMChannel_segment_RegExpValidator_text: "This field must only contain lower-case characters, numbers and dashes.",
  Validator_Abstract_Code_data_URL_property_must_be_set_text: "Data URL must be set if Data is not set.",
  Validator_Abstract_Code_code_property_must_be_set_text: "Data must be set if Data URL is not set.",
  ContentValidator_CMVideo_data_atLeastOneNotEmpty_text: "One of the following fields must be set: Data, Data URL.",
  ContentValidator_CMVideo_data_exactlyOneMustBeSet_text: "Just one of the following fields should be set: Data, Data URL",
  Validator_UniqueInSiteStringValidator_text: "This field must contain a value that is unique across all contents of the site. The same value is used in '{0}'.",
  Validator_UniqueStringValidator_text: "This field must contain a value that is unique across all contents. The same value is used in '{0}'.",
  Validator_no_cmpicture_text: "Only pictures are supported in this field.",
  CMNavigation_hidden_text: "This item is hidden and therefore not visible on the Website.",
  ValidationStatus_not_valid_anymore: "Invalid since",
  ValidationStatus_will_be_active: "Will be valid as of",
  Validator_validFrom_is_after_validTo_text: "The Valid From date is after the Valid To date.",
  Validator_validFrom_equals_validTo_text: "The document is never valid as the Valid From date equals the Valid To date.",
  Validator_placement_visibleFrom_is_after_visibleTo_text: "The Visible From date for content item '{0}' is after the Visible To date.",
  Validator_placement_visibleFrom_equals_visibleTo_text: "The document is never visible as the Visible From date equals the Visible To date.",
  Validator_placement_visibility_not_within_validity_text: "The visibility range set for content item '{0}' is not within the content's validity range.",
};

export default Validation_properties;
