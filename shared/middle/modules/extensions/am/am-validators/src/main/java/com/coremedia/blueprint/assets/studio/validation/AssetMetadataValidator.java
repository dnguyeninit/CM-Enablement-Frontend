package com.coremedia.blueprint.assets.studio.validation;

import com.coremedia.blueprint.base.config.ConfigurationService;
import com.coremedia.blueprint.base.config.StructConfiguration;
import com.coremedia.cap.struct.Struct;
import com.coremedia.cms.assets.AssetConstants;
import com.coremedia.rest.validation.Issues;
import com.coremedia.rest.validation.PropertyValidator;
import com.coremedia.rest.validation.Severity;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * A Validator for the metadata struct of assets.
 * Creates an issue for each channel or region that is set in the metadata struct of the content,
 * but is not configured in the asset management settings document.
 *
 * @see com.coremedia.blueprint.assets.AssetManagementConfigurationProperties#getSettingsDocument()
 */
public class AssetMetadataValidator implements PropertyValidator {
  public static final String ISSUE_CODE_METADATA_PROPERTY_NOT_OF_TYPE_STRUCT = "METADATA_PROPERTY_NOT_OF_TYPE_STRUCT";
  public static final String ISSUE_CODE_UNKNOWN_CHANNEL = "UNKNOWN_CHANNEL";
  public static final String ISSUE_CODE_UNKNOWN_REGION = "UNKNOWN_REGION";

  private String metadataProperty;
  private ConfigurationService configurationService;
  private String settingsDocument;
  private Set<String> categories = Collections.emptySet();

  public void setMetadataProperty(String metadataProperty) {
    this.metadataProperty = metadataProperty;
  }

  public void setConfigurationService(ConfigurationService configurationService) {
    this.configurationService = configurationService;
  }

  public void setSettingsDocument(String settingsDocument) {
    this.settingsDocument = settingsDocument;
  }

  @Override
  public String getProperty() {
    return metadataProperty;
  }

  @Override
  public void validate(Object value, Issues issues) {
    if (value == null) {
      return;
    }

    if (!(value instanceof Struct)) {
      issues.addIssue(categories, Severity.ERROR, getProperty(), ISSUE_CODE_METADATA_PROPERTY_NOT_OF_TYPE_STRUCT, metadataProperty);
      return;
    }

    StructConfiguration structMaps = configurationService.getStructMaps(null, settingsDocument, "settings");
    Map<String, Object> globalStructs = structMaps.getGlobalStructs();
    if (globalStructs == null) {
      return;
    }
    List<String> settingsChannels = (List<String>) globalStructs.get(AssetConstants.METADATA_CHANNELS_PROPERTY_NAME);
    List<String> settingsRegions = (List<String>) globalStructs.get(AssetConstants.METADATA_REGIONS_PROPERTY_NAME);
    if (settingsChannels == null || settingsRegions == null) {
      return;
    }

    Struct struct = (Struct) value;
    List<String> actualChannels = getStringList(struct, AssetConstants.METADATA_CHANNELS_PROPERTY_NAME);
    List<String> actualRegions = getStringList(struct, AssetConstants.METADATA_REGIONS_PROPERTY_NAME);

    for (String channel : actualChannels) {
      if (!settingsChannels.contains(channel)) {
        issues.addIssue(categories, Severity.WARN,
                metadataProperty + "." + AssetConstants.METADATA_CHANNELS_PROPERTY_NAME + "." + channel,
                ISSUE_CODE_UNKNOWN_CHANNEL,
                channel);
      }
    }

    for (String region : actualRegions) {
      if (!settingsRegions.contains(region)) {
        issues.addIssue(categories, Severity.WARN,
                metadataProperty + "." + AssetConstants.METADATA_REGIONS_PROPERTY_NAME + "." + region,
                ISSUE_CODE_UNKNOWN_REGION,
                region);
      }
    }
  }

  private static List<String> getStringList(Struct struct, String property) {
    return struct.getType().getDescriptor(property) != null
            ? struct.getStrings(property)
            : Collections.<String>emptyList();
  }
  /**
   * Set the categories to use for invalid values. Use the constants {@link Issues#CONTENT_ISSUE_CATEGORY} and
   * {@link Issues#LOCALIZATION_ISSUE_CATEGORY} as category values.
   *
   * @param categories the categories
   */
  protected void setCategories(Set<String> categories) {
    this.categories = categories == null ? Collections.emptySet() : Collections.unmodifiableSet(categories);
  }
}
