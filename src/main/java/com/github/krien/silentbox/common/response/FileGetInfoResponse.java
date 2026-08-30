package com.github.krien.silentbox.common.response;

import lombok.Data;

@Data
public class FileGetInfoResponse {

    private long total;
    private long selfTotal;
    private long selfPublicTotal;
    private long selfProtectTotal;
    private long selfNoTokenTotal;
    private long publicTotal;

}
