/*
 * Copyright 2014-2015 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.rockagen.gnext.service.spring.security.extension;

import com.rockagen.commons.util.CommUtil;
import org.springframework.security.web.authentication.WebAuthenticationDetails;

import javax.servlet.http.HttpServletRequest;

/**
 * An extension for spring security resolve the remoteAddress
 * 
 * @author RA
 * @since JDK1.7
 * @since 3.0
 */
public class ExWebAuthenticationDetails extends WebAuthenticationDetails {

	// ~ Instance fields ==================================================

	/**
	 * 
	 */
	private static final long serialVersionUID = 8522362828655932804L;
	/** The remote address. */
	private final String remoteAddress;

	// ~ Constructors
	// ===================================================================================================

	/**
	 * Records the remote address and will also set the session Id if a session
	 * already exists (it won't create one).
	 * 
	 * @param request
	 *            that the authentication request was received from
	 */
	public ExWebAuthenticationDetails(HttpServletRequest request) {

		super(request);
		remoteAddress = getRealAddr(request);
	}

	/**
	 * Get the real Address
	 * 
	 * @param request
	 * @return
	 * @throws Exception
	 */
	protected String getRealAddr(final HttpServletRequest request) {

		String customerIp = "";
		// If proxies by nginx apache ...
		String temp1 = request.getHeader("X-Real-IP");

		if (!CommUtil.isBlank(temp1)) {
			customerIp = temp1;
		} else {
			// use default
			customerIp = request.getRemoteAddr();
		}
		return customerIp;
	}

	/**
	 * Indicates the TCP/IP address the authentication request was received
	 * from.
	 * 
	 * @return the address
	 */
	@Override
	public String getRemoteAddress() {
		return remoteAddress;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof WebAuthenticationDetails) {
			WebAuthenticationDetails rhs = (WebAuthenticationDetails) obj;

			if ((getRemoteAddress() == null) && (rhs.getRemoteAddress() != null)) {
				return false;
			}

			if ((getRemoteAddress() != null) && (rhs.getRemoteAddress() == null)) {
				return false;
			}

			if (getRemoteAddress() != null) {
				if (!getRemoteAddress().equals(rhs.getRemoteAddress())) {
					return false;
				}
			}

			if ((getSessionId() == null) && (rhs.getSessionId() != null)) {
				return false;
			}

			if ((getSessionId() != null) && (rhs.getSessionId() == null)) {
				return false;
			}

			if (getSessionId() != null) {
				if (!getSessionId().equals(rhs.getSessionId())) {
					return false;
				}
			}

			return true;
		}

		return false;
	}
	
    public int hashCode() {
        int code = 1618;

        if (getRemoteAddress() != null) {
            code = code * (getRemoteAddress().hashCode() % 7);
        }

        if (getSessionId() != null) {
            code = code * (getSessionId().hashCode() % 7);
        }

        return code;
    }
}
