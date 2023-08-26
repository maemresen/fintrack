package com.maemresen.fintrack.api.test.util.performer;

import com.maemresen.fintrack.api.test.util.RequestConfig;
import org.springframework.test.web.servlet.ResultActions;

public interface Perform {
    ResultActions perform(RequestConfig requestConfig) throws Exception;
}
