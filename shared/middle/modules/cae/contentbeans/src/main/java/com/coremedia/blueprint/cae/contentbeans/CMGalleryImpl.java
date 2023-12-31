package com.coremedia.blueprint.cae.contentbeans;

import com.coremedia.blueprint.common.contentbeans.CMMedia;

/**
 * Generated extension class for immutable beans of document type "CMGallery".
 */
public class CMGalleryImpl<T extends CMMedia> extends CMGalleryBase<T> {

  @Override
  public String getFeedTitle() {
    String title = getTitle();
    if (title == null) {
      title = getTeaserTitle();
    }
    if (title == null) {
      title = getContent().getName();
    }
    return title;
  }
}
