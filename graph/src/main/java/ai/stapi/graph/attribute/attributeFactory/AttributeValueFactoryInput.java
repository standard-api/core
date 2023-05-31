package ai.stapi.graph.attribute.attributeFactory;

public class AttributeValueFactoryInput {
    
    private final Object value;
    private final String dataType;

    public AttributeValueFactoryInput(Object value, String dataType) {
        this.value = value;
        this.dataType = dataType;
    }

    public Object getValue() {
        return value;
    }

    public String getDataType() {
        return dataType;
    }
}
