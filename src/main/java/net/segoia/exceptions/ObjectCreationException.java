/**
 * commons - Various Java Utils
 * Copyright (C) 2009  Adrian Cristian Ionescu - https://github.com/acionescu
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package net.segoia.exceptions;


public class ObjectCreationException extends Exception {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -4162922035286921184L;

	public ObjectCreationException() {
		super();
	}

	public ObjectCreationException(Throwable e) {
		super(e);
	}

	public ObjectCreationException(String ex) {
		super(ex);
	}

	public ObjectCreationException(String message, Throwable e) {
		super(message, e);
	}
}
