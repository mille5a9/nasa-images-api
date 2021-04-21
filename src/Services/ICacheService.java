package Services;

import java.util.ArrayList;

public interface ICacheService<keyType, responseType> {

    public void Clean();
    public void Clear();
    public void Write(keyType key, responseType response);
    public ArrayList<responseType> Read(keyType key);
}
