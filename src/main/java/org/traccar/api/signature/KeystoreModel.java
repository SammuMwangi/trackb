
package org.traccar.api.signature;

import org.traccar.model.BaseModel;
import org.traccar.storage.StorageName;

@StorageName("tc_keystore")
public class KeystoreModel extends BaseModel {

    private byte[] publicKey;

    public byte[] getPublicKey() {
        return publicKey;
    }

    public void setPublicKey(byte[] publicKey) {
        this.publicKey = publicKey;
    }

    private byte[] privateKey;

    public byte[] getPrivateKey() {
        return privateKey;
    }

    public void setPrivateKey(byte[] privateKey) {
        this.privateKey = privateKey;
    }

}
