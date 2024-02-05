package sk.tuke.meta.persistence.utils.sqlgenerator;

import sk.tuke.meta.persistence.utils.metadata.ClassMetaData;
import sk.tuke.meta.persistence.utils.metadata.MetaData;

import javax.persistence.Id;
import javax.persistence.ManyToOne;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.*;

public class SQLGenerator {
    // tableMetaData.getByKeyMetaDataList("fields")
    public static Map<String,String> createDSL(List<MetaData> listMetaData){

        Map<String,String> classToDSL = new HashMap<>();

        for(MetaData metaData: listMetaData){
            StringBuilder input = new StringBuilder("nameOfField { " + metaData.getNameDeclaredField() + " }\ntypeOfField { " + metaData.getTypeDeclaredField() + " }\nannotationsOfField { ");

            for(Annotation annotation: metaData.getAnnotations())
                input.append(annotation.annotationType().getSimpleName()).append(" ");
            input.append("}");

            classToDSL.put(metaData.getNameDeclaredField(), input.toString());
        }

        return classToDSL;
    }

    public static String createTable(ClassMetaData tableMetaData,  Map<String,Map<String,String>> classesToDSL){

        StringBuilder generatedColumn= new StringBuilder();
        generatedColumn.append("CREATE TABLE IF NOT EXISTS "+tableMetaData.getClassName().toLowerCase()+" (\n");
        for(MetaData metaData: tableMetaData.getByKeyMetaDataList("fields")) {

            Lexer lexer = new Lexer(
                    classesToDSL
                            .get(tableMetaData.getClassName())
                            .get(metaData.getNameDeclaredField())
            );

            Parser parser = new Parser(
                    lexer,
                    classesToDSL
            );

            SQLQueryDefinition definition = parser.sqlQueryDefinition();

            generatedColumn.append(generateColumn(definition));

        }

        generatedColumn.replace(generatedColumn.length()-2,generatedColumn.length()-1,"\n);");


        return generatedColumn.toString();
    }

    private static String generateColumn(SQLQueryDefinition definition){
        StringBuilder query= new StringBuilder(definition.getName() + " " + definition.getType() + " ");
        for (String annotation: definition.getAnnotations())
           query.append(annotation);
        query.append(",\n");
        return String.valueOf(query);
    }

}
