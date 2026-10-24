package app.revanced.patches.forkgram.ads

import app.revanced.patcher.extensions.addInstructions
import app.revanced.patcher.firstMethod
import app.revanced.patcher.patch.bytecodePatch

// Written for ReVanced Patcher 22.
// MessagesController is kept by Forkgram's ProGuard rules
// (-keep class org.telegram.messenger.* { *; }), so the class and
// method names stay stable across releases.

@Suppress("unused")
val hideSponsoredMessagesPatch = bytecodePatch(
    name = "Hide sponsored messages",
    description = "Stops the app from requesting and showing sponsored messages in channels.",
) {
    // No version given = applies to any version.
    compatibleWith("org.forkgram.classic")

    apply {
        val method = firstMethod {
            definingClass == "Lorg/telegram/messenger/MessagesController;" &&
                name == "getSponsoredMessages" &&
                parameterTypes.size == 1 &&
                parameterTypes[0] == "J"
        }

        // Return null right away. The original method already returns null
        // for chats that can't have sponsored messages, so every caller
        // handles it.
        method.addInstructions(
            0,
            """
                const/4 v0, 0x0
                return-object v0
            """,
        )
    }
}