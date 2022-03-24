import BeanFactoryImpl from "@coremedia/studio-client.client-core-impl/data/impl/BeanFactoryImpl";
import AbstractRemoteTest from "@coremedia/studio-client.client-core-test-helper/AbstractRemoteTest";
import ValueExpressionFactory from "@coremedia/studio-client.client-core/data/ValueExpressionFactory";
import beanFactory from "@coremedia/studio-client.client-core/data/beanFactory";
import EditorContextImpl from "@coremedia/studio-client.main.editor-components/sdk/EditorContextImpl";
import Ext from "@jangaroo/ext-ts";
import Viewport from "@jangaroo/ext-ts/container/Viewport";
import Assert from "@jangaroo/joounit/flexunit/framework/Assert";
import { bind, cast } from "@jangaroo/runtime";
import joo from "@jangaroo/runtime/joo";
import { AnyFunction } from "@jangaroo/runtime/types";
import AnalyticsStudioPluginBase from "../src/AnalyticsStudioPluginBase";
import OpenAnalyticsHomeUrlButton from "../src/OpenAnalyticsHomeUrlButton";

class OpenAnalyticsHomeUrlButtonTest extends AbstractRemoteTest {

  #button: OpenAnalyticsHomeUrlButton = null;

  #args: any = null;

  #window_open: AnyFunction = null;

  #is_sub_object: AnyFunction = null;

  #beanImplPrototype: any;

  static isSubObject(value: any, propertyPath: any): boolean {
    // Plain objects represent sub-beans.
    return value.constructor === Object;
  }

  override setUp(): void {
    super.setUp();

    BeanFactoryImpl.initBeanFactory();

    EditorContextImpl.initEditorContext();

    (AnalyticsStudioPluginBase as unknown)["SETTINGS"] = ValueExpressionFactory.create("testSettings");
    AnalyticsStudioPluginBase.SETTINGS.setValue(beanFactory._.createLocalBean({ properties: { settings: { testService: {} } } }));

    this.#beanImplPrototype = joo.getQualifiedObject("com.coremedia.ui.data.impl.BeanImpl")["prototype"];
    this.#is_sub_object = this.#beanImplPrototype.isSubObject;
    this.#beanImplPrototype.isSubObject = OpenAnalyticsHomeUrlButtonTest.isSubObject;
    this.#window_open = bind(window, window.open);
    window.open = ((... myArgs): Window => {
      this.#args = myArgs;
      return window;
    });
    this.#button = Ext.create(OpenAnalyticsHomeUrlButton, { serviceName: "testService" });
    // show buttons
    Ext.create(Viewport, { items: [ this.#button ] });
  }

  override tearDown(): void {
    OpenAnalyticsHomeUrlButtonTest.setHomeUrlValue(null);
    window.open = this.#window_open;
    this.#args = null;
    this.#beanImplPrototype.isSubObject = this.#is_sub_object;
    super.tearDown();
  }

  static setHomeUrlValue(value: string): void {
    AnalyticsStudioPluginBase.SETTINGS.extendBy("properties.settings.testService.homeUrl").setValue(value);
  }

  testInitialState(): void {
    Assert.assertTrue(this.#button.disabled);
  }

  testSetHomeUrls(): void {
    OpenAnalyticsHomeUrlButtonTest.setHomeUrlValue("http://fake.url");
    this.waitUntil("button still disabled",
      (): boolean =>
        !this.#button.disabled
      ,
      cast(Function, this.#button.handler), // simulate click
    );
    this.waitUntil("home url not opened",
      (): boolean => this.#args !== null && this.#args[0] == "http://fake.url",
      (): void =>
        OpenAnalyticsHomeUrlButtonTest.setHomeUrlValue("yetAnotherInvalidUrl"),

    );
    this.waitUntil("button still enabled",
      (): boolean =>
        this.#button.disabled,

    );
  }

}

export default OpenAnalyticsHomeUrlButtonTest;
