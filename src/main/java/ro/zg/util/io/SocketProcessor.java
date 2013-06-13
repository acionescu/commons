/*******************************************************************************
 * Copyright 2011 Adrian Cristian Ionescu
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package ro.zg.util.io;

import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import ro.zg.util.processing.Processor;

public class SocketProcessor implements Processor<Socket, Boolean>{
    private SocketWorkerFactory socketWorkerFactory;
    private ExecutorService executor = Executors.newCachedThreadPool();
    
    public Boolean process(Socket input) throws Exception {
	SocketWorker worker = socketWorkerFactory.createSocketWorker(input);
	executor.execute(worker);
	return true;
    }

    public SocketWorkerFactory getSocketWorkerFactory() {
        return socketWorkerFactory;
    }

    public void setSocketWorkerFactory(SocketWorkerFactory socketWorkerFactory) {
        this.socketWorkerFactory = socketWorkerFactory;
    }

    public ExecutorService getExecutor() {
        return executor;
    }

    public void setExecutor(ExecutorService executor) {
        this.executor = executor;
    }
}
