package sk.tuke.meta.persistence.utils.metadata;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class MetaData {

    private String typeDeclaredField;
    private String nameDeclaredField;
    private List<Annotation> annotations;

    private Field fieldData;

    public MetaData() {
    }

    public MetaData(Field fieldData, String typeDeclaredField, String nameDeclaredField) {
        this.fieldData=fieldData;
        this.typeDeclaredField = typeDeclaredField;
        this.nameDeclaredField = nameDeclaredField;
    }

    public MetaData(Field fieldData,String typeDeclaredField, String nameDeclaredField, List<Annotation> annotations) {
        this(fieldData,typeDeclaredField,nameDeclaredField);
        this.annotations = annotations;
    }

    public String getTypeDeclaredField() {
        return typeDeclaredField;
    }

    public String getNameDeclaredField() {
        return nameDeclaredField;
    }

    public List<Annotation> getAnnotations() {
        return annotations;
    }

    public Field getFieldData() {
        return fieldData;
    }

}
