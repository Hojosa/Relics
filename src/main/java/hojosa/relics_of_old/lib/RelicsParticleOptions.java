package hojosa.relics_of_old.lib;


import java.util.function.Supplier;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import lombok.Getter;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.FriendlyByteBuf;

public class RelicsParticleOptions implements ParticleOptions {

	public static final Deserializer<RelicsParticleOptions> DESERIALIZER = new Deserializer<>() {
        public RelicsParticleOptions fromCommand(ParticleType<RelicsParticleOptions> type, StringReader reader) throws CommandSyntaxException {
            reader.expect(' ');
            int lifetime = reader.readInt();
            reader.expect(' ');
            float size = reader.readFloat();
            return new RelicsParticleOptions(() -> type, lifetime, size);
        }

        public RelicsParticleOptions fromNetwork(ParticleType<RelicsParticleOptions> type, FriendlyByteBuf buf) {
            return new RelicsParticleOptions(() -> type, buf.readInt(), buf.readFloat(), buf.readFloat(), buf.readFloat(), buf.readFloat());
        }
    };

    public static Codec<RelicsParticleOptions> codec(Supplier<ParticleType<RelicsParticleOptions>> type) {
        return RecordCodecBuilder.create(instance ->
                instance.group(
                        Codec.INT.fieldOf("lifetime").forGetter(o -> o.lifetime),
                        Codec.FLOAT.fieldOf("size").forGetter(o -> o.size)
                ).apply(instance, (lifetime, size) -> new RelicsParticleOptions(type, lifetime, size)));
    }
    
    private final Supplier<ParticleType<RelicsParticleOptions>> type;
    private final int lifetime;
    private final float size;
    @Getter
    private final float r;
    @Getter
    private final float g;
    @Getter
    private final float b;

    public RelicsParticleOptions(Supplier<ParticleType<RelicsParticleOptions>> type, int lifetime, float size) {
        this(type, lifetime, size, 1f, 1f, 0.4f);
  }

    public RelicsParticleOptions(Supplier<ParticleType<RelicsParticleOptions>> type, int lifetime, float size, float r, float g, float b) {
          this.type = type;
          this.lifetime = lifetime;
          this.size = size;
          this.r = r;
          this.g = g;
          this.b = b;
    }

    public int getLifetime() {
        return lifetime;
    }

    public float getSize() {
        return size;
    }

    @Override
    public ParticleType<?> getType() {
        return type.get();
    }

    @Override
    public void writeToNetwork(FriendlyByteBuf buf) {
        buf.writeInt(lifetime);
        buf.writeFloat(size);
        buf.writeFloat(r);
        buf.writeFloat(g);
        buf.writeFloat(b);
    }

    @Override
    public String writeToString() {
        return String.format("%s %d %.2f", BuiltInRegistries.PARTICLE_TYPE.getKey(getType()), lifetime, size);
    }
}