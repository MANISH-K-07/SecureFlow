package secureflow.rules;

import com.google.gson.Gson;

import java.io.FileReader;
import java.util.Set;
import java.util.Map;

public class RuleConfig {

    // Sink methods
    public Set<String> dangerous_methods;

    // Sanitizer methods
    public Set<String> sanitizer_methods;

    // Severity per sink (e.g., exec -> HIGH)
    public Map<String, String> severity;

    public static RuleConfig load(String path) throws Exception {
        Gson gson = new Gson();
        return gson.fromJson(new FileReader(path), RuleConfig.class);
    }
}
