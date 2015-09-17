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
package net.segoia.db.sql;

import java.net.URLEncoder;

public class SqlUtil {
    public static String sanitizeStringValues(String input) {
	return input.replace("'", "''").replace("\\", "\\\\");
    }

    public static void main(String[] args) throws Exception {
	System.out.println(sanitizeStringValues("http://kotaku.com/5353358/the-secret-world-which-side-are-you-on/gallery/\\/\\/kotaku.com%5C/5351836%5C/the-secret-world-secrets-surface-beta-access-teased\\"));
	System.out.println(URLEncoder.encode("http://kotaku.com/5353358/the-secret-world-which-side-are-you-on/gallery/\\/\\/kotaku.com\\/5351836%5C/the-secret-world-secrets-surface-beta-access-teased\\","ISO-8859-1"));
    }
}
