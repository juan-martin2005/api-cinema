package com.jg.sistema.movie.application.port.out;

import java.io.IOException;
import java.util.Map;

public interface CloudStorePort {
    Map<String, String> upload (Object file, Map params) throws IOException;
}
