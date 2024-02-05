package sk.tuke.meta.persistence.utils.metadata;

import java.util.Dictionary;
import java.util.Hashtable;
import java.util.List;

public class ClassMetaData {

    private Dictionary<String, List<MetaData>> classMetaData= new Hashtable<>();

    private final String className;

    public ClassMetaData(String className) {
        this.className=className;
    }

    public ClassMetaData(String className,Dictionary<String, List<MetaData>> classMetaData) {
        this(className);
        this.classMetaData = classMetaData;
    }

    public Dictionary<String, List<MetaData>> getClassMetaData() {
        return classMetaData;
    }

    public void setClassMetaData(Dictionary<String, List<MetaData>> classMetaData) {
        this.classMetaData = classMetaData;
    }

    public void put(String key, List<MetaData> metaDataList){
        this.classMetaData.put(key,metaDataList);
    }

    public List<MetaData> getByKeyMetaDataList(String key){
        return this.classMetaData.get(key);
    }

    public String getClassName() {
        return className;
    }
}
