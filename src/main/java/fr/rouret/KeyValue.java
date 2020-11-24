package fr.rouret;
import com.google.gson.annotations.SerializedName;
/**
 * @author Baptiste MAQUET on 23/11/2020
 * @project tets
 */
public class KeyValue<K,V> {
    @SerializedName("paramName") 
    private K key;
    @SerializedName("paramType") 
    private V value;

    public KeyValue(K key, V value) {
        this.key = key;
        this.value = value;
    }

    public K getKey() {
        return key;
    }

    public void setKey(K key) {
        this.key = key;
    }

    public V getValue() {
        return value;
    }

    public void setValue(V value) {
        this.value = value;
    }
}
