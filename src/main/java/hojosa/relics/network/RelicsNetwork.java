package hojosa.relics.network;

import hojosa.relics.lib.RelicsUtil;
import net.minecraftforge.network.NetworkDirection;
import slimeknights.mantle.network.NetworkWrapper;


public class RelicsNetwork extends NetworkWrapper{
	private static RelicsNetwork instance = null;
	  
    public RelicsNetwork() {
		super(RelicsUtil.mcLoc("network"));
	}
    
    public static RelicsNetwork getInstance() {
        if (instance == null) {
          throw new IllegalStateException("Attempt to call network getInstance before network is setup");
        }
        return instance;
      }
    
    public static void register() {
        if (instance != null) {
            return;
          }
        instance = new RelicsNetwork();
        
        instance.registerPacket(PhoenixParticlePacket.class, PhoenixParticlePacket::new, NetworkDirection.PLAY_TO_CLIENT);
    }
}
