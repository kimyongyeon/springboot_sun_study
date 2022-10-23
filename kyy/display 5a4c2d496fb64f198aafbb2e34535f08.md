# display

# 충돌

롬복에서 만든 imageUrl과 내가 임의의로 만든 imageURL 변수가 충돌이 일어나 imageURL을 get할 수 없는 현상을 마주치게 되었다. 

- 상당히 당황스럽다.
- 인터넷에서 검색해도 안나온다.
- 문제 해결방법을 찾기 위해서 디버깅을 해도 풀 수 없었다.
- 처음에는 separator가 문제인가?
- 인코딩을 하지 말아볼까?
- 생성자를 직접 만들어 볼까?

```java
package kyy.springbootkumongcodingproject.dto.common;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.File;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLEncoder;

@Data
public class UploadResultDTO implements Serializable {
    private String fileName;
    private String uuid;

    private String folderPath;

    private String imageUrl;

    public UploadResultDTO(String fileName, String uuid, String folderPath) {
        this.fileName = fileName;
        this.uuid = uuid;
        this.folderPath = folderPath;
        this.imageUrl = imageUrlMaker();
    }

    // 롬복과 imageURL 충돌로 이름 변경 DTO에 만든 데이터가 생산되지 않음.
    private String getImageURL() {
        try {
            return URLEncoder.encode(this.folderPath + File.separator + this.uuid + "_" + this.fileName, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return "";
    }
}
```