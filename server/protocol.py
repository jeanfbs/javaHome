# protocol.py
import json


class HomeSocketRequest:
    def __init__(self, method, path, version, headers, body):
        self.method = method
        self.path = path
        self.version = version
        self.headers = headers
        self.body = body


class HomeSocketResponse:
    def __init__(self, method, path, version, headers, body):
        self.method = method
        self.path = path
        self.version = version
        self.headers = headers
        self.body = body

    def to_message(self):
        header_lines = [
            f"{self.method} {self.path} {self.version}"
        ]
        for k, v in self.headers.items():
            header_lines.append(f"{k}: {v}")

        return "\n".join(header_lines) + "\n\n\n" + json.dumps(self.body, separators=(",", ":"))


def parse_request(message: str) -> HomeSocketRequest:
    header_part, body_part = message.split("\n\n\n", 1)

    lines = header_part.split("\n")
    method, path, version = lines[0].split()

    headers = {}
    for line in lines[1:]:
        key, value = line.split(":", 1)
        headers[key.strip()] = value.strip()

    body = json.loads(body_part) if body_part.strip() else {}

    return HomeSocketRequest(method, path, version, headers, body)