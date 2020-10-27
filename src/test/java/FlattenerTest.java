import com.itextpdf.text.DocumentException;
import com.scrive.pdftools.tools.flattener.FlattenerSpec;
import com.scrive.pdftools.tools.flattener.LambdaFlattener;
import org.apache.commons.io.IOUtils;
import org.json.JSONObject;
import org.junit.Assert;
import org.junit.Test;

import java.io.*;
import java.nio.charset.StandardCharsets;

public class FlattenerTest {


    @Test
    public void shouldFlattenFields() throws IOException, DocumentException {
        final JSONObject specToFlatten = new JSONObject(IOUtils.resourceToString("/flat_test_filled.json", StandardCharsets.UTF_8));
        final FlattenerSpec fileToFlattenSpec = FlattenerSpec.fromJson(specToFlatten);
        final byte[] fileToCheck = fileToFlattenSpec.getFileContent();
        //Check that the PdfFormField "name" is present in the document.
        Assert.assertTrue(TestUtils.checkDocumentIsFlat(fileToCheck));

        final byte[] fileAfterFlattened = LambdaFlattener.execute(fileToFlattenSpec);
        final byte[] fileFlattened = fileAfterFlattened;
        //Check that the PdfFormField "name" is not present anymore.
        Assert.assertFalse(TestUtils.checkDocumentIsFlat(fileFlattened));
    }
}
