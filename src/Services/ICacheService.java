package Services;

public interface ICacheService<keyType, responseType> {

    public void Clean();
    public void Clear();
    public void Write(keyType key, responseType response);
    public String Read(keyType key);
}
