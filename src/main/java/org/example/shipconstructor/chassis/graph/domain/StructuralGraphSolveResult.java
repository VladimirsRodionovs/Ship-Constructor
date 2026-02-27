package org.example.shipconstructor.chassis.graph.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class StructuralGraphSolveResult {
    private final List<StructuralGraphMemberEnvelope> memberEnvelopes;
    private final List<StructuralGraphPanelEnvelope> panelEnvelopes;
    private final double memberParticipationShare;
    private final double panelParticipationShare;
    private final String loadSharingProfileLabel;
    private final List<String> warnings;

    public StructuralGraphSolveResult(
            List<StructuralGraphMemberEnvelope> memberEnvelopes,
            List<StructuralGraphPanelEnvelope> panelEnvelopes,
            double memberParticipationShare,
            double panelParticipationShare,
            String loadSharingProfileLabel,
            List<String> warnings) {
        this.memberEnvelopes = memberEnvelopes == null
                ? Collections.<StructuralGraphMemberEnvelope>emptyList()
                : Collections.unmodifiableList(new ArrayList<StructuralGraphMemberEnvelope>(memberEnvelopes));
        this.panelEnvelopes = panelEnvelopes == null
                ? Collections.<StructuralGraphPanelEnvelope>emptyList()
                : Collections.unmodifiableList(new ArrayList<StructuralGraphPanelEnvelope>(panelEnvelopes));
        this.memberParticipationShare = memberParticipationShare;
        this.panelParticipationShare = panelParticipationShare;
        this.loadSharingProfileLabel = loadSharingProfileLabel;
        this.warnings = warnings == null
                ? Collections.<String>emptyList()
                : Collections.unmodifiableList(new ArrayList<String>(warnings));
    }

    public List<StructuralGraphMemberEnvelope> getMemberEnvelopes() { return memberEnvelopes; }
    public List<StructuralGraphPanelEnvelope> getPanelEnvelopes() { return panelEnvelopes; }
    public double getMemberParticipationShare() { return memberParticipationShare; }
    public double getPanelParticipationShare() { return panelParticipationShare; }
    public String getLoadSharingProfileLabel() { return loadSharingProfileLabel; }
    public List<String> getWarnings() { return warnings; }
}
