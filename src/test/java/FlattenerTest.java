import com.itextpdf.text.DocumentException;
import com.scrive.pdftools.tools.flattener.FlattenerSpec;
import com.scrive.pdftools.tools.flattener.LambdaFlattener;
import org.apache.commons.io.IOUtils;
import org.json.JSONObject;
import org.junit.Assert;
import org.junit.Test;

import java.io.*;
import java.nio.charset.Charset;

public class FlattenerTest {
    @Test
    public void shouldFlattenFields() throws IOException, DocumentException {
        final JSONObject specToFlatten = new JSONObject(IOUtils.resourceToString("/flat_test_filled.json", Charset.defaultCharset()));
        final FlattenerSpec fileToFlattenSpec = FlattenerSpec.fromJson(specToFlatten);
        final ByteArrayInputStream fileToCheck = new ByteArrayInputStream(fileToFlattenSpec.getMainFileInput().getContent());
        //Check that the PdfFormField "name" is present in the document.
        Assert.assertTrue(TestUtils.checkDocumentIsFlat(fileToCheck));

        final ByteArrayOutputStream fileAfterFlattened = LambdaFlattener.execute(fileToFlattenSpec);
        final InputStream fileFlattened = new ByteArrayInputStream(fileAfterFlattened.toByteArray());
        //Check that the PdfFormField "name" is not present anymore.
        Assert.assertFalse(TestUtils.checkDocumentIsFlat(fileFlattened));
    }
}
