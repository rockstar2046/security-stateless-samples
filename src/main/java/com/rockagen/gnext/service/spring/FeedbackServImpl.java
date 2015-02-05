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
package com.rockagen.gnext.service.spring;

import com.rockagen.gnext.po.Feedback;
import com.rockagen.gnext.service.FeedbackServ;
import org.springframework.stereotype.Service;

/**
 * Implementation of the {@link FeedbackServ} interface*
 * @author ra
 * @since JDK1.8
 */
@Service("feedbackServ")
public class FeedbackServImpl extends QueryObjectGenericServImpl<Feedback,Long> implements FeedbackServ {
}
