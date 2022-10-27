package co.teamloops.loopvouchers.registry;

import co.teamloops.commons.patterns.Registry;
import co.teamloops.loopvouchers.object.Voucher;
import lombok.Getter;

import java.util.HashMap;

public class VoucherRegistry implements Registry<String, Voucher> {

    @Getter private final HashMap<String, Voucher> registry = new HashMap<>();

}
