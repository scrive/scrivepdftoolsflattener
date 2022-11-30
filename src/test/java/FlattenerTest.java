import com.scrive.pdftools.tools.flattener.FlattenerSpec;
import com.scrive.pdftools.tools.flattener.LambdaFlattener;
import org.json.JSONObject;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.*;

public class FlattenerTest {
    @BeforeClass
    public static void InitItextLicense() {
        TestUtils.initITextLicence();
    }

    @Test
    public void shouldFlattenFields() throws IOException {
        final InputStream fileToFlatten = new FileInputStream(TestUtils.inputFile("flat_test_filled.json"));
        final JSONObject specToFlatten = new JSONObject(TestUtils.fromStream(fileToFlatten));
        final FlattenerSpec fileToFlattenSpec = FlattenerSpec.fromJson(specToFlatten);
        final ByteArrayInputStream fileToCheck = new ByteArrayInputStream(fileToFlattenSpec.mainFileInput.getContent());
        //Check that the PdfFormField "name" is present in the document.
        Assert.assertFalse(TestUtils.checkDocumentIsFlat(fileToCheck));

        final ByteArrayOutputStream fileAfterFlattened = LambdaFlattener.execute(fileToFlattenSpec);
        final InputStream fileFlattened = new ByteArrayInputStream(fileAfterFlattened.toByteArray());
        //Check that the PdfFormField "name" is not present anymore.
        Assert.assertTrue(TestUtils.checkDocumentIsFlat(fileFlattened));
    }

}
