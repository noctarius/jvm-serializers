package serializers;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;

import com.github.lightning.Lightning;
import com.github.lightning.base.AbstractSerializerDefinition;

import data.media.Image;
import data.media.Media;
import data.media.MediaContent;

public class LightningSerialization {

	public static void register(TestGroups groups) {
		groups.media.add(JavaBuiltIn.mediaTransformer, new LightningSerializer<MediaContent>());
	}

	public static class LightningSerializer<T> extends Serializer<T> {

		private final com.github.lightning.Serializer serializer;

		public LightningSerializer() {
			serializer = Lightning.newBuilder().serializerDefinitions(new MediaSerializerDefinition()).debugCacheDirectory(new File("debug")).build();
		}

		@Override
		public T deserialize(byte[] array) throws Exception {
			return (T) serializer.deserialize(new ByteArrayInputStream(array));
		}

		@Override
		public byte[] serialize(T content) throws Exception {
			ByteArrayOutputStream baos = outputStream(content);
			serializer.serialize(content, baos);
			return baos.toByteArray();
		}

		@Override
		public String getName() {
			return "Lightning";
		}

	}

	public static class MediaSerializerDefinition extends AbstractSerializerDefinition {

		@Override
		protected void configure() {
			serialize(Media.class).attributes(attribute("uri"), attribute("title"), attribute("width"), attribute("height"), attribute("format"),
					attribute("duration"), attribute("size"), attribute("bitrate"), attribute("hasBitrate"), attribute("persons"), attribute("player"),
					attribute("copyright"));

			serialize(Image.class).attributes(attribute("uri"), attribute("title"), attribute("width"), attribute("height"), attribute("size"));

			serialize(MediaContent.class).attributes(attribute("media"), attribute("images"));

			serialize(MediaContent[].class);
		}
	}

}
