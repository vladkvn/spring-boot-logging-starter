package testapp;

import java.util.UUID;
public class BankConfigBase {
    protected UUID trustedTransportCertificateId;
    protected UUID trustedSigningCertificateId;

    protected UUID trialTransportCertifivateId;
    protected UUID trialSigningCertifivateId;

    public UUID getTrustedTransportCertificateId() {
        return trustedTransportCertificateId;
    }

    public void setTrustedTransportCertificateId(UUID trustedTransportCertificateId) {
        this.trustedTransportCertificateId = trustedTransportCertificateId;
    }

    public UUID getTrustedSigningCertificateId() {
        return trustedSigningCertificateId;
    }

    public void setTrustedSigningCertificateId(UUID trustedSigningCertificateId) {
        this.trustedSigningCertificateId = trustedSigningCertificateId;
    }

    public UUID getTrialTransportCertifivateId() {
        return trialTransportCertifivateId;
    }

    public void setTrialTransportCertifivateId(UUID trialTransportCertifivateId) {
        this.trialTransportCertifivateId = trialTransportCertifivateId;
    }

    public UUID getTrialSigningCertifivateId() {
        return trialSigningCertifivateId;
    }

    public void setTrialSigningCertifivateId(UUID trialSigningCertifivateId) {
        this.trialSigningCertifivateId = trialSigningCertifivateId;
    }

    public String getPassPhrase()
    {
        return null;
    }
}
