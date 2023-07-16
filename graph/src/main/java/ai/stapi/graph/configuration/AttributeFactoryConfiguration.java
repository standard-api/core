package ai.stapi.graph.configuration;

import ai.stapi.graph.attribute.attributeFactory.AttributeFactory;
import ai.stapi.graph.attribute.attributeFactory.GenericAttributeFactory;
import ai.stapi.graph.attribute.attributeFactory.LeafAttributeFactory;
import ai.stapi.graph.attribute.attributeFactory.ListAttributeFactory;
import ai.stapi.graph.attribute.attributeFactory.SetAttributeFactory;
import ai.stapi.graph.attribute.attributeFactory.attributeValueFactory.AttributeValueFactory;
import ai.stapi.graph.attribute.attributeFactory.attributeValueFactory.Base64BinaryValueFactory;
import ai.stapi.graph.attribute.attributeFactory.attributeValueFactory.BooleanValueFactory;
import ai.stapi.graph.attribute.attributeFactory.attributeValueFactory.CanonicalValueFactory;
import ai.stapi.graph.attribute.attributeFactory.attributeValueFactory.CodeValueFactory;
import ai.stapi.graph.attribute.attributeFactory.attributeValueFactory.DateAttributeValueFactory;
import ai.stapi.graph.attribute.attributeFactory.attributeValueFactory.DateTimeAttributeValueFactory;
import ai.stapi.graph.attribute.attributeFactory.attributeValueFactory.DecimalValueFactory;
import ai.stapi.graph.attribute.attributeFactory.attributeValueFactory.GenericAttributeValueFactory;
import ai.stapi.graph.attribute.attributeFactory.attributeValueFactory.IdValueFactory;
import ai.stapi.graph.attribute.attributeFactory.attributeValueFactory.InstantAttributeValueFactory;
import ai.stapi.graph.attribute.attributeFactory.attributeValueFactory.IntegerValueFactory;
import ai.stapi.graph.attribute.attributeFactory.attributeValueFactory.MarkdownValueFactory;
import ai.stapi.graph.attribute.attributeFactory.attributeValueFactory.OidValueFactory;
import ai.stapi.graph.attribute.attributeFactory.attributeValueFactory.PositiveIntegerValueFactory;
import ai.stapi.graph.attribute.attributeFactory.attributeValueFactory.StringValueFactory;
import ai.stapi.graph.attribute.attributeFactory.attributeValueFactory.UnknownValueFactory;
import ai.stapi.graph.attribute.attributeFactory.attributeValueFactory.UnsignedIntegerValueFactory;
import ai.stapi.graph.attribute.attributeFactory.attributeValueFactory.UriValueFactory;
import ai.stapi.graph.attribute.attributeFactory.attributeValueFactory.UrlValueFactory;
import ai.stapi.graph.attribute.attributeFactory.attributeValueFactory.UuidValueFactory;
import ai.stapi.graph.attribute.attributeFactory.attributeValueFactory.XhtmlValueFactory;
import java.util.List;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.context.annotation.Bean;

@AutoConfiguration
public class AttributeFactoryConfiguration {

  @Bean
  public LeafAttributeFactory leafAttributeFactory(
      GenericAttributeValueFactory genericAttributeValueFactory
  ) {
    return new LeafAttributeFactory(genericAttributeValueFactory);
  }

  @Bean
  public ListAttributeFactory listAttributeFactory(
      GenericAttributeValueFactory genericAttributeValueFactory
  ) {
    return new ListAttributeFactory(genericAttributeValueFactory);
  }

  @Bean
  public SetAttributeFactory setAttributeFactory(
      GenericAttributeValueFactory genericAttributeValueFactory
  ) {
    return new SetAttributeFactory(genericAttributeValueFactory);
  }
  
  @Bean
  public GenericAttributeFactory genericAttributeFactory(
      List<AttributeFactory> attributeFactories
  ) {
    return new GenericAttributeFactory(attributeFactories);
  }
  
  @Bean
  public Base64BinaryValueFactory base64BinaryValueFactory() {
    return new Base64BinaryValueFactory();
  }

  @Bean
  public BooleanValueFactory booleanValueFactory() {
    return new BooleanValueFactory();
  }

  @Bean
  public CanonicalValueFactory canonicalValueFactory() {
    return new CanonicalValueFactory();
  }

  @Bean
  public CodeValueFactory codeValueFactory() {
    return new CodeValueFactory();
  }

  @Bean
  public DateAttributeValueFactory dateAttributeValueFactory() {
    return new DateAttributeValueFactory();
  }

  @Bean
  public DateTimeAttributeValueFactory dateTimeAttributeValueFactory() {
    return new DateTimeAttributeValueFactory();
  }

  @Bean
  public DecimalValueFactory decimalValueFactory() {
    return new DecimalValueFactory();
  }

  @Bean
  public IdValueFactory idValueFactory() {
    return new IdValueFactory();
  }

  @Bean
  public InstantAttributeValueFactory instantAttributeValueFactory() {
    return new InstantAttributeValueFactory();
  }

  @Bean
  public IntegerValueFactory integerValueFactory() {
    return new IntegerValueFactory();
  }

  @Bean
  public MarkdownValueFactory markdownValueFactory() {
    return new MarkdownValueFactory();
  }

  @Bean
  public OidValueFactory oidValueFactory() {
    return new OidValueFactory();
  }

  @Bean
  public PositiveIntegerValueFactory positiveIntegerValueFactory() {
    return new PositiveIntegerValueFactory();
  }

  @Bean
  public StringValueFactory stringValueFactory() {
    return new StringValueFactory();
  }

  @Bean
  public UnknownValueFactory unknownValueFactory() {
    return new UnknownValueFactory();
  }

  @Bean
  public UnsignedIntegerValueFactory unsignedIntegerValueFactory() {
    return new UnsignedIntegerValueFactory();
  }

  @Bean
  public UriValueFactory uriValueFactory() {
    return new UriValueFactory();
  }

  @Bean
  public UrlValueFactory urlValueFactory() {
    return new UrlValueFactory();
  }

  @Bean
  public UuidValueFactory uuidValueFactory() {
    return new UuidValueFactory();
  }

  @Bean
  public XhtmlValueFactory xhtmlValueFactory() {
    return new XhtmlValueFactory();
  }

  @Bean
  public GenericAttributeValueFactory genericAttributeValueFactory(
      List<AttributeValueFactory> attributeValueFactories
  ) {
    return new GenericAttributeValueFactory(attributeValueFactories);
  }
}
