/*
 * Copyright 2020-2099 sa-token.cc
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.wtf.crossorgin.main.model;

import cn.hutool.core.util.StrUtil;
import lombok.Builder;
import lombok.Data;

import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;


@Data
@Builder
public class SaCookie {

    /**
     * 写入响应头时使用的key
     */
    public static final String HEADER_NAME = "Set-Cookie";

    /**
     * 名称
     */
    private String name;

    /**
     * 值
     */
    private String value;

    /**
     * 有效时长 （单位：秒），-1 代表为临时Cookie 浏览器关闭后自动删除
     */
    @Builder.Default
    private int maxAge = -1;

    /**
     * 域
     */
    private String domain;

    /**
     * 路径
     */
    @Builder.Default
    private String path = "/";

    /**
     * 是否只在 https 协议下有效
     */
    @Builder.Default
    private Boolean secure = false;

    /**
     * 是否禁止 js 操作 Cookie
     */
    @Builder.Default
    private Boolean httpOnly = false;

    /**
     * 第三方限制级别（Strict=完全禁止，Lax=部分允许，None=不限制）
     */
    private String sameSite;

    /**
     * 转换为响应头 Set-Cookie 参数需要的值
     *
     * @return /
     */
    public String toHeaderValue() {
        StringBuilder sb = new StringBuilder();
        sb.append(name).append("=").append(value);

        if (maxAge >= 0) {
            sb.append("; Max-Age=").append(maxAge);
            String expires;
            if (maxAge == 0) {
                expires = Instant.EPOCH.atOffset(ZoneOffset.UTC).format(DateTimeFormatter.RFC_1123_DATE_TIME);
            } else {
                expires = OffsetDateTime.now().plusSeconds(maxAge).format(DateTimeFormatter.RFC_1123_DATE_TIME);
            }
            sb.append("; Expires=").append(expires);
        }
        if (StrUtil.isNotBlank(domain)) {
            sb.append("; Domain=").append(domain);
        }
        if (StrUtil.isNotBlank(path)) {
            sb.append("; Path=").append(path);
        }
        if (secure) {
            sb.append("; Secure");
        }
        if (httpOnly) {
            sb.append("; HttpOnly");
        }
        if (StrUtil.isNotBlank(sameSite)) {
            sb.append("; SameSite=").append(sameSite);
        }
        return sb.toString();
    }

}
