package de.mauricius17.teamspeak.verifier.system;

import com.github.theholywaffle.teamspeak3.TS3Query;
import com.github.theholywaffle.teamspeak3.api.event.ClientJoinEvent;
import com.github.theholywaffle.teamspeak3.api.event.TS3EventAdapter;
import com.github.theholywaffle.teamspeak3.api.event.TS3EventType;
import de.mauricius17.teamspeak.verifier.utils.Messages;
import de.mauricius17.teamspeak.verifier.utils.Utils;

import java.util.Arrays;
import java.util.List;

/**
 * Created by SirWill on 28.03.2016.
 */
public class WelcomeMessage {

    public static void main() {

        if (Utils.getTeamSpeakCfg().getBoolean("welcome_message")) {

            TS3Query ts3query = TeamSpeakVerifier.getTs3query();

            ts3query.getApi().registerEvent(TS3EventType.SERVER);

            ts3query.getApi().addTS3Listeners(new TS3EventAdapter() {
                @Override
                public void onClientJoin(ClientJoinEvent e) {
                    final int clientid = e.getClientId();
                    final List<String> groups = Arrays.asList(e.getClientServerGroups().split(","));

                    if (!groups.contains("43")) {
                        ts3query.getApi().sendPrivateMessage(clientid, Messages.WELCOME_MESSAGE.getMessage());
                    }
                }
            });
        }
    }
}
