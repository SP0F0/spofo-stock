package spofo.stock.schedule.task;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.util.IOUtils;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import spofo.stock.data.request.publicdata.Item;

@Service
@RequiredArgsConstructor
public class AmazonS3Service {

    private static final String DEFAULT_IMAGE_NAME = "default";
    private static final String DEFAULT_IMAGE_EXTENSION = ".jpeg";
    private static final String IMAGE_EXTENSION = ".png";
    private static final String IMAGE_LOCATION = "images/logo/kr/";

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    @Value("${image.s3-url}")
    private String s3Url;

    @Value("${image.origin-url}")
    private String originUrl;

    private final AmazonS3 amazonS3;

    public Map<String, String> uploadLogos(List<Item> itemList) {

        Map<String, String> savedImageUrl = new HashMap<>();

        for (Item item : itemList) {

            URL url = null;

            try {
                String imageKey = IMAGE_LOCATION + "t" + item.getSrtnCd() + IMAGE_EXTENSION;

                if (amazonS3.doesObjectExist(IMAGE_LOCATION, imageKey)) continue;

                url = new URL(originUrl + item.getSrtnCd() + IMAGE_EXTENSION);

                InputStream inputStream = url.openStream();
                byte[] bytes = IOUtils.toByteArray(inputStream);
                ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes);

                ObjectMetadata objectMetadata = new ObjectMetadata();
                objectMetadata.setContentType(IMAGE_EXTENSION);
                objectMetadata.setContentLength(bytes.length);

                amazonS3.putObject(
                        new PutObjectRequest(bucket, imageKey, byteArrayInputStream, objectMetadata));

                savedImageUrl.put(item.getSrtnCd(), s3Url + imageKey);

            } catch (IOException e) {
                String imageKey = IMAGE_LOCATION + DEFAULT_IMAGE_NAME + DEFAULT_IMAGE_EXTENSION;
                savedImageUrl.put(item.getSrtnCd(), s3Url + imageKey);
            }
        }

        return savedImageUrl;
    }
}
