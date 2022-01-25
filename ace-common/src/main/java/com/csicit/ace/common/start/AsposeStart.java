package com.csicit.ace.common.start;

import com.aspose.cells.License;
import org.springframework.stereotype.Component;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

/**
 * @author FourLeaves
 * @version V1.0
 * @date 2020/4/10 16:46
 */
@Component
public class AsposeStart implements IAceAppStartToDo {

    private final static String licenseStr = "<License><Data><LicensedTo>IARI DEEPSOFT TECHNOLOGY CO., LTD</LicensedTo><EmailTo>huchangping1@sina.com</EmailTo><LicenseType>Developer " +
            "OEM</LicenseType><LicenseNote>Limited to 1 developer, unlimited physical locations</LicenseNote><OrderID>181121011033</OrderID><UserID>134983271</UserID><OEM>This is a redistributable license</OEM><Products><Product>Aspose.Total Product Family</Product></Products><EditionType>Enterprise</EditionType><SerialNumber>1072000f-ecb2-4c41-8fad-a0ed1548eec7</SerialNumber><SubscriptionExpiry>20191121</SubscriptionExpiry><LicenseVersion>3.0</LicenseVersion><LicenseInstructions>https://purchase.aspose.com/policies/use-license</LicenseInstructions></Data><Signature>AxmKMg/ujmWls/dGQz+Pqtlb9ko/U0FhjozJzsj6QIYyGTUVCVCcZR7PgW2vcIdNh/E+4x1dQAx7qyv78e1rPaoNkr74M60nZfLzIXVssjOByEPdymzQsKIc6vUbl2299vg+h4gsnmJgGrwsFuxpDUFSHqcS1CyKk2czl7NbpoM=</Signature></License>";

    @Override
    public void run() {
//        try {
//            InputStream license = new ByteArrayInputStream(licenseStr.getBytes());
//            License aposeLic = new License();
//            aposeLic.setLicense(license);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
    }
}
