package net.alloymc.mod.griefprevention.claim;

/**
 * Result of a claim creation attempt.
 *
 * @param succeeded      true if the claim was created
 * @param claim          the new claim (only when succeeded)
 * @param failureReason  human-readable reason (only when failed)
 * @param overlapping    the existing claim that caused the overlap (null if not an overlap failure)
 */
public record CreateClaimResult(boolean succeeded, Claim claim, String failureReason, Claim overlapping) {

    public static CreateClaimResult success(Claim claim) {
        return new CreateClaimResult(true, claim, null, null);
    }

    public static CreateClaimResult failure(String reason) {
        return new CreateClaimResult(false, null, reason, null);
    }

    public static CreateClaimResult overlapFailure(String reason, Claim overlapping) {
        return new CreateClaimResult(false, null, reason, overlapping);
    }
}
