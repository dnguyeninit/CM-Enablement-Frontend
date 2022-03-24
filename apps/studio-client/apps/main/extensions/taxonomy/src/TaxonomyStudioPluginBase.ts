import UserUtil from "@coremedia/studio-client.cap-base-models/util/UserUtil";
import IdHelper from "@coremedia/studio-client.cap-rest-client/common/IdHelper";
import session from "@coremedia/studio-client.cap-rest-client/common/session";
import Content from "@coremedia/studio-client.cap-rest-client/content/Content";
import ValueExpressionFactory from "@coremedia/studio-client.client-core/data/ValueExpressionFactory";
import StringHelper from "@coremedia/studio-client.ext.ui-components/util/StringHelper";
import StudioPlugin from "@coremedia/studio-client.main.editor-components/configuration/StudioPlugin";
import IEditorContext from "@coremedia/studio-client.main.editor-components/sdk/IEditorContext";
import PreviewURI from "@coremedia/studio-client.main.editor-components/sdk/preview/PreviewURI";
import { as } from "@jangaroo/runtime";
import Config from "@jangaroo/runtime/Config";
import { AnyFunction } from "@jangaroo/runtime/types";
import TaxonomyStudioPlugin from "./TaxonomyStudioPlugin";
import TopicsHelper from "./TopicsHelper";
import TaxonomyUtil from "./taxonomy/TaxonomyUtil";

interface TaxonomyStudioPluginBaseConfig extends Config<StudioPlugin> {
}

class TaxonomyStudioPluginBase extends StudioPlugin {
  declare Config: TaxonomyStudioPluginBaseConfig;

  constructor(config: Config<TaxonomyStudioPlugin> = null) {
    super(config);
  }

  override init(editorContext: IEditorContext): void {
    super.init(editorContext);
    editorContext.registerPreviewUrlTransformer((uri: PreviewURI, callback: AnyFunction): void => {
      const source = as(uri.getSource(), Content);
      ValueExpressionFactory.createFromFunction(TopicsHelper.resolveTaxonomyForTopicPage, source).loadValue(
        (taxonomy: Content): void => {
          if (taxonomy) {
            uri.appendParameter("taxonomyId", IdHelper.parseContentId(taxonomy));
          }
          callback.call(null);
        },
      );
    });
  }

  /**
   * Returns true if the current user can administrate the taxonomies.
   * @return
   */
  static isAdministrationEnabled(callback: AnyFunction): void {
    //initially requesting if the admin tab is enabled
    TaxonomyUtil.loadSettings((adminGroups: Array<any>): void => {
      if (session._.getUser().isAdministrative()) {
        callback.call(null, true);
      } else {
        for (let i = 0; i < adminGroups.length; i++) {
          const groupName = StringHelper.trim(adminGroups[i], "");
          if (UserUtil.isInGroup(groupName)) {
            callback.call(null, true);
            return;
          }
        }
        callback.call(null, false);
      }
    });
  }
}

export default TaxonomyStudioPluginBase;
