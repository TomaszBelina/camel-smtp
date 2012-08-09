/****************************************************************
 * Licensed to the Apache Software Foundation (ASF) under one   *
 * or more contributor license agreements.  See the NOTICE file *
 * distributed with this work for additional information        *
 * regarding copyright ownership.  The ASF licenses this file   *
 * to you under the Apache License, Version 2.0 (the            *
 * "License"); you may not use this file except in compliance   *
 * with the License.  You may obtain a copy of the License at   *
 *                                                              *
 *   http://www.apache.org/licenses/LICENSE-2.0                 *
 *                                                              *
 * Unless required by applicable law or agreed to in writing,   *
 * software distributed under the License is distributed on an  *
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY       *
 * KIND, either express or implied.  See the License for the    *
 * specific language governing permissions and limitations      *
 * under the License.                                           *
 ****************************************************************/
package org.apache.camel.component.james.smtp;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import org.apache.camel.Component;
import org.apache.camel.Endpoint;
import org.apache.camel.impl.DefaultComponent;
import org.apache.camel.util.ObjectHelper;

// TODO: Auto-generated Javadoc
/**
 * The Class SMTPComponent.
 * 
 * {@link Component} which create new {@link SMTPEndpoint} instances
 */
public class SMTPComponent extends DefaultComponent {

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.apache.camel.impl.DefaultComponent#createEndpoint(java.lang.String,
	 * java.lang.String, java.util.Map)
	 */
	@Override
	protected Endpoint createEndpoint(String uri, String remaining,
			Map<String, Object> parameters) throws Exception {
		SMTPURIConfiguration config = new SMTPURIConfiguration(new URI(uri));

		String domainString = getAndRemoveParameter(parameters, "localDomains",
				String.class);
		if (ObjectHelper.isNotEmpty(domainString)) {

			StringTokenizer tokenizer = new StringTokenizer(domainString, ",");
			List<String> localDomains = new ArrayList<String>();
			while (tokenizer.hasMoreTokens()) {
				localDomains.add(tokenizer.nextToken().trim());
			}
			config.setLocalDomains(localDomains);
		}
		setProperties(config, parameters);
		return new SMTPEndpoint(remaining, this, config);
	}

}
