package com.coremedia.blueprint.themeimporter;

import com.coremedia.cap.content.Content;
import com.coremedia.cap.content.ContentRepository;
import com.coremedia.cap.content.ContentType;
import com.coremedia.cap.struct.Struct;
import com.coremedia.cap.struct.StructBuilder;
import com.coremedia.cap.struct.StructService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Calendar;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class MapToStructAdapterTest {

  @Mock
  private ContentRepository contentRepository;

  @Mock
  private StructService structService;

  @Mock
  private StructBuilder structBuilder;

  @Mock
  private Struct struct;

  @Mock
  private ContentType contentType;

  private MapToStructAdapter adapter;

  @Before
  public void setUp() {
    adapter = new MapToStructAdapter(structService, contentRepository);
    when(contentRepository.getContentContentType()).thenReturn(contentType);
    when(structService.createStructBuilder()).thenReturn(structBuilder);
    when(structBuilder.build()).thenReturn(struct);
  }

  @Test
  public void testGetStructWithEmptyMap() {
    assertSame(struct, adapter.getStruct(Map.of()));

    InOrder inOrder = inOrder(structBuilder);
    inOrder.verify(structBuilder).build();
    inOrder.verifyNoMoreInteractions();
  }

  @Test
  public void testGetStructWithBasicTypes() {
    Content someContent = mock(Content.class);
    Calendar someDate = mock(Calendar.class);
    Map<String, Object> json = new LinkedHashMap<>();
    json.put("string", "def");
    json.put("integer", 5);
    json.put("boolean", true);
    json.put("link", someContent);
    json.put("date", someDate);

    assertSame(struct, adapter.getStruct(json));

    InOrder inOrder = inOrder(structBuilder);
    inOrder.verify(structBuilder).declareString("string", Integer.MAX_VALUE, "def");
    inOrder.verify(structBuilder).declareInteger("integer", 5);
    inOrder.verify(structBuilder).declareBoolean("boolean", true);
    inOrder.verify(structBuilder).declareLink("link", contentType, someContent);
    inOrder.verify(structBuilder).declareDate("date", someDate);
    inOrder.verify(structBuilder).build();
    inOrder.verifyNoMoreInteractions();
  }

  @Test
  public void testGetStructMultipleCalls() {
    assertSame(struct, adapter.getStruct(Map.of("string", "def")));
    assertSame(struct, adapter.getStruct(Map.of()));

    InOrder inOrder = inOrder(structBuilder);
    inOrder.verify(structBuilder).declareString("string", Integer.MAX_VALUE, "def");
    inOrder.verify(structBuilder, times(2)).build();
    inOrder.verifyNoMoreInteractions();
  }

  @Test
  public void testGetStructWithNestedObject() {
    Map<String, Object> json = new LinkedHashMap<>();
    json.put("subJson", Map.of("string", "def"));
    json.put("subJson2", Map.of("subSubJson2", Map.of("integer", 5)));

    assertSame(struct, adapter.getStruct(json));

    InOrder inOrder = inOrder(structBuilder);
    inOrder.verify(structBuilder).enter("subJson");
    inOrder.verify(structBuilder).declareString("string", Integer.MAX_VALUE, "def");
    inOrder.verify(structBuilder).up();
    inOrder.verify(structBuilder).enter("subJson2");
    inOrder.verify(structBuilder).enter("subSubJson2");
    inOrder.verify(structBuilder).declareInteger("integer", 5);
    inOrder.verify(structBuilder, times(2)).up();
    inOrder.verify(structBuilder).build();
    inOrder.verifyNoMoreInteractions();
  }

  @Test
  public void testGetStructWithArray() {
    Content someContent = mock(Content.class);
    Content someOtherContent = mock(Content.class);
    Calendar someDate = mock(Calendar.class);
    Calendar someOtherDate = mock(Calendar.class);
    Map<String, Object> json = new LinkedHashMap<>();
    json.put("stringList", List.of("a", "b"));
    json.put("integerList", List.of(1, 2));
    json.put("booleanList", List.of(true, false));
    json.put("linkList", List.of(someContent, someOtherContent));
    json.put("dateList", List.of(someDate, someOtherDate));

    assertSame(struct, adapter.getStruct(json));

    InOrder inOrder = inOrder(structBuilder);
    inOrder.verify(structBuilder).declareStrings("stringList", Integer.MAX_VALUE, List.of());
    inOrder.verify(structBuilder).add("stringList", "a");
    inOrder.verify(structBuilder).add("stringList", "b");
    inOrder.verify(structBuilder).declareIntegers("integerList", List.of());
    inOrder.verify(structBuilder).add("integerList", 1);
    inOrder.verify(structBuilder).add("integerList", 2);
    inOrder.verify(structBuilder).declareBooleans("booleanList", List.of());
    inOrder.verify(structBuilder).add("booleanList", true);
    inOrder.verify(structBuilder).add("booleanList", false);
    inOrder.verify(structBuilder).declareLinks("linkList", contentType, List.of());
    inOrder.verify(structBuilder).add("linkList", someContent);
    inOrder.verify(structBuilder).add("linkList", someOtherContent);
    inOrder.verify(structBuilder).declareDates("dateList", List.of());
    inOrder.verify(structBuilder).add("dateList", someDate);
    inOrder.verify(structBuilder).add("dateList", someOtherDate);
    inOrder.verify(structBuilder).build();
    inOrder.verifyNoMoreInteractions();
  }
}
