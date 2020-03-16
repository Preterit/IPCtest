package com.study.ipc;

// Declare any non-default types here with import statements
import com.study.ipc.model.Request;
import com.study.ipc.model.Response;
interface IIPCService {

    Response send(in Request request);
}
