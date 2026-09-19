SOURCES := $(shell find . -name '*.java' -not -path './build/*' -not -path './Clock/*')
BUILD   := build
STAMP   := $(BUILD)/.compiled

.PHONY: test run clean

test: $(STAMP)
	java -cp $(BUILD) Test

run: test

$(STAMP): $(SOURCES)
	mkdir -p $(BUILD)
	javac -d $(BUILD) $(SOURCES)
	touch $(STAMP)

clean:
	rm -rf $(BUILD)
