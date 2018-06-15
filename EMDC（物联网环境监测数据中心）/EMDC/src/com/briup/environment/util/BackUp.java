package com.briup.environment.util;

import java.io.FileNotFoundException;
import java.io.IOException;

public interface BackUp extends EMDCModule{
    void store(String filePath, Object obj,boolean append) throws Exception;
    Object load(String filePath, boolean del) throws Exception;

}
