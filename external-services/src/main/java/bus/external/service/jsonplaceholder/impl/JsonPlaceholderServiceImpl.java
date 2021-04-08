package bus.external.service.jsonplaceholder.impl;

import bus.external.service.jsonplaceholder.IJsonPlaceholderService;
import io.github.harishb2k.easy.http.util.Call;
import io.github.harishb2k.easy.http.util.EasyHttp;

import java.util.Map;

public class JsonPlaceholderServiceImpl implements IJsonPlaceholderService {
    @Override
    public Map getPost(int id) {
        return EasyHttp.callSync(
                Call.builder(Map.class)
                        .withServerAndApi("jsonplaceholder", "getPosts")
                        .addPathParams("id", 1)
                        .build()
        );
    }
}
