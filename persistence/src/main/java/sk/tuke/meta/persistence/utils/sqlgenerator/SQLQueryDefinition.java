package sk.tuke.meta.persistence.utils.sqlgenerator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SQLQueryDefinition {

    private  List<String> annotations = new ArrayList<>();
    private  String type ="";
    private  String name ="";
    private  String oldType ="";
    public void addAnnotation(String annotationValue) {
        annotations.add(annotationValue);
    }
    public void addType(String typeValue) {
        type=typeValue;
    }
    public void addOldType(String oldTypeValue) {
        oldType=oldTypeValue;
    }

    public void addName(String nameValue) {
        name=nameValue;
    }

    public List<String> getAnnotations() {
        return annotations;
    }

    public String getType() {
        return type;
    }

    public String getOldType() {
        return oldType;
    }

    public String getName() {
        return name;
    }
}
