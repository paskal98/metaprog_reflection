package sk.tuke.meta.persistence.utils.parsers;

import sk.tuke.meta.persistence.utils.metadata.ClassMetaData;
import sk.tuke.meta.persistence.utils.metadata.MetaData;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.*;

public class ObjectFiledParser {


    public static Dictionary<String, List<MetaData>> parseFieldMetaData(Class<?> classObj){

        Dictionary<String, List<MetaData>> classMetaData = new Hashtable<>();
        List<MetaData> fields = new ArrayList<>();

        for(Field field: classObj.getDeclaredFields()) {

            List<Annotation> annotations = new ArrayList<>();

            field.getAnnotations();
            for (Annotation annotation: field.getAnnotations())
                annotations.add(annotation);

            MetaData metaData = new MetaData(
                    field,
                    field.getType().getSimpleName(),
                    field.getName(),
                    annotations
            );

            fields.add(metaData);

        }

        classMetaData.put("fields",fields);
        return classMetaData;
    }



}
