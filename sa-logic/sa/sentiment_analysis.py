from kafka import KafkaConsumer, KafkaProducer
from textblob import TextBlob
import json
import os

BOOTSTRAP = os.getenv("KAFKA_BOOTSTRAP", "kafka:9092")
REQ_TOPIC = os.getenv("REQ_TOPIC", "sa.requests")
REP_TOPIC = os.getenv("REP_TOPIC", "sa.replies")

producer = KafkaProducer(
    bootstrap_servers=BOOTSTRAP,
    value_serializer=lambda v: json.dumps(v).encode("utf-8"),
)

consumer = KafkaConsumer(
    REQ_TOPIC,
    bootstrap_servers=BOOTSTRAP,
    value_deserializer=lambda b: json.loads(b.decode("utf-8")),
    enable_auto_commit=True,
    group_id=os.getenv("GROUP_ID", "logic-group"),
)

def handle(msg):
    # msg: {"sentence": "...", "correlationId":"..."}
    sentence = msg["sentence"]
    polarity = TextBlob(sentence).sentences[0].polarity if sentence.strip() else 0.0
    out = {"sentence": sentence, "polarity": polarity, "correlationId": msg["correlationId"]}
    producer.send(REP_TOPIC, out)
    producer.flush()

if __name__ == "__main__":
    for record in consumer:
        handle(record.value)
