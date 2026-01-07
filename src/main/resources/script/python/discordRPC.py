from pypresence import Presence
import time
import sys

client_id = "1458209604457463909" 
RPC = Presence(client_id)

RPC.connect()

print("RPC Connected!")

# Arguments: [1] state, [2] details
state_info = sys.argv[1] if len(sys.argv) > 1 else "Using the launcher"
details_info = sys.argv[2] if len(sys.argv) > 2 else "In main menu"

play_in = "Playing in " + sys.argv[3] if len(sys.argv) > 3 else "Until he plays"
url_game = sys.argv[4] if len(sys.argv) > 4 else "http://store.steampowered.com"

RPC.update(
    state=state_info,
    details=details_info,
    buttons=[
        {"label": play_in, "url": url_game},
        {"label": "Launcher GitHub", "url": "https://github.com/NotionMe/games-launcher"}
    ]
)

print(f"Status updated: {state_info} - {details_info}")

try:
    while True:
        time.sleep(15)
except KeyboardInterrupt:
    print("RPC Disconnected!")