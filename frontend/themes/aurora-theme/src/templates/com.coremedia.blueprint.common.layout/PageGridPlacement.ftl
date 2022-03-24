<#-- @ftlvariable name="self" type="com.coremedia.blueprint.common.layout.PageGridPlacement" -->

<#assign renderEmpty=cm.localParameters().renderEmpty!false />

<#if renderEmpty || self.items?has_content || self.name == "main" || self.name == "header">
  <div id="cm-placement-${self.name!""}"
       class="cm-placement cm-placement--${self.name!""}"<@preview.metadata data=[bp.getPlacementPropertyName(self), bp.getPlacementHighlightingMetaData(self)!""]/>>
    <#-- replace main section with the main content to render -->
    <#if self.name! == "main" && cmpage.detailView>
      <@cm.include self=cmpage.content/>
    <#-- do not display the above section if in detailView -->
    <#elseif self.name! == "above" && cmpage.detailView>
      <#-- do nothing -->
    <#-- render the placement items -->
    <#-- sidebar -->
    <#elseif self.name! == "sidebar">
      <#list self.items![] as item>
        <@cm.include self=item view="asSquareBanner" />
      </#list>
    <#-- default -->
    <#else>
      <@cm.include self=self view="asContainer" />
    </#if>
  </div>
</#if>
