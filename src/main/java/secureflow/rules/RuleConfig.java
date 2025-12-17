package secureflow.rules;

import com.google.gson.Gson;
import java.io.FileReader;
import java.util.Set;

public class RuleConfig {

    public Set<String> dangerous_methods;
    public Set<String> sanitizer_methods;

    public static RuleConfig load(String path) throws Exception {
        Gson gson = new Gson();
        return gson.fromJson(new FileReader(path), RuleConfig.class);
    }
}
