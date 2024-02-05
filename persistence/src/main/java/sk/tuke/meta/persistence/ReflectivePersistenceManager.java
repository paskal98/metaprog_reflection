package sk.tuke.meta.persistence;

import sk.tuke.meta.persistence.utils.parsers.ObjectFiledParser;
import sk.tuke.meta.persistence.utils.metadata.ClassMetaData;
import sk.tuke.meta.persistence.utils.sqlgenerator.SQLGenerator;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class ReflectivePersistenceManager implements PersistenceManager {

    private final Connection connection;
    private Class<?>[] types;

    private List<ClassMetaData> classesMetaData;


    public ReflectivePersistenceManager(Connection connection, Class<?>... types) {
        this.connection=connection;
        this.types=types;
        this.classesMetaData = new ArrayList<>();
    }

    @Override
    public void createTables() {

        //parsed field
        for(Class<?> classObj: types)
            parseClassField(classObj);

        // create DSL from CLasses
        Map<String,Map<String,String>> classDSL = new HashMap<>();
        for(ClassMetaData classObjData: classesMetaData)
            classDSL.put(
                    classObjData.getClassName(),
                    SQLGenerator.createDSL(classObjData.getClassMetaData().get("fields"))
            );


        //get if table exists by class name
        for(ClassMetaData classObjData: classesMetaData) {
            try {
                PreparedStatement preparedStatement = connection.prepareStatement("SELECT name FROM sqlite_master WHERE type='table' AND name=?");
                preparedStatement.setString(1, classObjData.getClassName().toLowerCase());
                ResultSet response = preparedStatement.executeQuery();
                if (!response.next()) {
                    //create table
                    System.out.println("Table " + classObjData.getClassName().toLowerCase() + " not exists. Create table...\n");

                    System.out.println(SQLGenerator.createTable(classObjData,classDSL));
                    preparedStatement=connection.prepareStatement(SQLGenerator.createTable(classObjData,classDSL));
                    preparedStatement.execute();
                } else {
                    System.out.println("Table " + classObjData.getClassName().toLowerCase() + " already exists");
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            System.out.println();
        }


    }

    @Override
    public <T> Optional<T> get(Class<T> type, long id) {
        return Optional.empty();
    }

    @Override
    public <T> List<T> getAll(Class<T> type) {

        return Collections.emptyList();
    }

    @Override
    public <T> List<T> getBy(Class<T> type, String fieldName, Object value) {
        return Collections.emptyList();
    }

    @Override
    public long save(Object entity) {
        return 0;
    }

    @Override
    public void delete(Object entity) {
    }

    public void parseClassField(Class<?> classObj){
        ClassMetaData classMetaData = new ClassMetaData(
                classObj.getSimpleName(),
                ObjectFiledParser.parseFieldMetaData(classObj)
        );
        classesMetaData.add(classMetaData);
    }
}
